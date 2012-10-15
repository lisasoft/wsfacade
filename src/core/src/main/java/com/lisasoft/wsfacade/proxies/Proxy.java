/*
 * Copyright 2012 LISAsoft - lisasoft.com. 
 * All rights reserved.
 *
 * This file is part of Web Services Facade.
 *
 * Web Services Facade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Web Services Facade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Web Services Facade.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lisasoft.wsfacade.proxies;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.lisasoft.wsfacade.generators.HttpGenerator;
import com.lisasoft.wsfacade.interpreters.HttpInterpreter;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.security.ISecurityProvider;
import com.lisasoft.wsfacade.utils.Constants;
import com.lisasoft.wsfacade.utils.PropertiesUtil;

/**
 * A Proxy object which is loaded from the application context through spring
 * bindings.
 * 
 * @author jgroffen
 * @author jhudson
 */
public class Proxy {
	private static final Logger log = Logger.getLogger(Proxy.class);
	protected static final String PROXY_PREFIX = "/proxies";

	/**
	 * <p>
	 * The name of this proxy object for example "WFSProxy" set in the
	 * applicationContext.xml with the name property
	 * </p>
	 * <property name="name" value="kvpToSoap" />
	 */
	protected String name = null;

	/**
	 * <p>
	 * This is the context URL for instance if the proxy is accessible:
	 * </p>
	 * <p>
	 * <b>http://host:8080/proxy</b>
	 * </p>
	 * <p>
	 * This proxy will be available here:
	 * </p>
	 * <p>
	 * <b>http://host:8080/proxy/proxyContextUrl</b>
	 * </p>
	 */
	protected String proxyContextUrl = null;

	/**
	 * The URL of the service this proxy is responsible for pushing requests
	 * onto.
	 */
	protected String serviceUrl = null;

	/**
	 * A list of URLS which this proxy can handle internally, this means if the
	 * request from the client is a request to a URL in this list it will
	 * <b>NOT</b> be pushed to the service.
	 */
	protected Map<String, String> proxyManagedUrls = null;

	/**
	 * A security provider - can provide security implementations on top of the
	 * HttpClient Object.
	 */
	protected ISecurityProvider securityProvider = null;

	/**
	 * Interprets all incoming requests from clients
	 */
	private HttpInterpreter clientRequestInterpreter = null;

	/**
	 * Generates requests for the service URL in the format necessary to
	 * Communicate with it.
	 */
	private HttpGenerator serverRequestGenerator = null;
	
	/**
	 * Interprets the response from the service. 
	 */
	private HttpInterpreter serverResponseInterpreter = null;
	
	/**
	 * Creates an appropriate response for the originating client request.
	 */
	private HttpGenerator clientResponseGenerator = null;
	
	/**
	 * This is the type of request the proxy is expected to send to the server
	 */
	private String serviceRequestType = "get";

	/**
	 * The main method which will do all the 'real' work.
	 * @param serviceRequestType
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		URL host = new URL("http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath()
				+ proxyContextUrl);

		Preconditions.checkNotNull(clientRequestInterpreter,
				"Failed to load clientRequestInterpreter");
		Preconditions.checkNotNull(clientResponseGenerator,
				"Failed to load clientResponseGenerator");
		Preconditions.checkNotNull(serverRequestGenerator,
				"Failed to load serverRequestGenerator");
		Preconditions.checkNotNull(serverResponseInterpreter,
				"Failed to load serverResponseInterpreter");

		if (log.isDebugEnabled()) {
			log.debug(String
					.format("Request for Proxy (%s) recieved - passing it on to the clientInterpreter of type: (%s)",
							name, clientRequestInterpreter.getClass().getName()));
		}

		/*
		 * Remove the proxy prefix usually "/proxies", we only take what was
		 * after the proxy path and the query string.
		 */
		String requestedUrl = (PROXY_PREFIX + request.getPathInfo()).replace(
				proxyContextUrl, "") + "?" + request.getQueryString();

		/*
		 * Here we are checking if the request was a request to a proxy managed
		 * URL for instance "WSDL" - in this case we don't want to pass the
		 * request on to the server we can to handle it here at the proxy.
		 */
		if (getProxyManagedUrls().keySet().contains(requestedUrl)) {
			processProxyManagedUrl(host, requestedUrl, serviceRequestType,
					request, response);
		} else {
			/*
			 * Otherwise we are going to need to push the request back out to
			 * the service once we have translated it
			 */
			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 1: Interpret Client Request (%s)",
						clientRequestInterpreter.getClass().getName()));
			}

			/*
			 * The model object represents the data in the request in a generic
			 * format which can be translated into a new request type.
			 */
			IModel model = clientRequestInterpreter.interpretRequest(request);
	
			/*
			 * Give the model a copy of the host parameter in case its needed
			 */
			model.getProperties().put(Constants.HOST, host.toString());

			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 2: Generate Service Request (%s)",
						serverRequestGenerator.getClass().getName()));
			}

			/*
			 * At this point we want to create a new request in correct format
			 * what the server can handle. So here we invoke the
			 * serverRequestGenerator to create a request from the model.
			 */
			HttpRequestBase serverRequest = null;
			try {
				serverRequest = serverRequestGenerator.generateRequest(model,
						serviceUrl, serviceRequestType);
			} catch (UnsupportedModelException ume) {
				log.error(
						"UnsupportedModelException while generating server request.",
						ume);
				throw new ServletException(ume);
			} catch (URISyntaxException use) {
				log.error(
						"URISyntaxException while generating server request.",
						use);
				throw new ServletException(use);
			}

			/*
			 * OK so we have a new request - which the server can now understand
			 * - and we need to send the request so we can get the response
			 * back. You will notice the next line invokes the getHttpClient
			 * method, this method will get an Apache HttpClient which may or
			 * may not have a security provisioned. (please read the
			 * ISecuirtyProvider interface for more details)
			 */
			HttpClient httpClient = getHttpClient(request);
			HttpResponse serverResponse = httpClient.execute(serverRequest);

			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 3: Interpret Server Response (%s)",
						serverResponseInterpreter.getClass().getName()));
			}

			/*
			 * At this point the proxy has sent the request and received a
			 * response, we need to unwrap it and send it back to the client
			 * 
			 * here we are taking the same route as before but going backwards:
			 * take the response from the service and get a the model (remember
			 * its what holds the data in a nice generic way)
			 */
			model = serverResponseInterpreter.interpretResponse(serverResponse);
			model.getProperties().put("host", host.toString());

			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 4: Generate Client Response (%s)",
						clientResponseGenerator.getClass().getName()));
			}

			/*
			 * This is where we send the request back to the client
			 */
			try {
				clientResponseGenerator.generateResponse(model, response);
			} catch (UnsupportedModelException ume) {
				log.error(
						"UnsupportedModelException while generating response to client.",
						ume);
				throw new ServletException(ume);
			}
		}
	}

	/**
	 * This method is responsible to loading the HttpClient with security
	 * credentials or a DefaultHttpClient if no ISecurityProvider is provided
	 * for this proxy.
	 * 
	 * @param request
	 * @return
	 */
	private HttpClient getHttpClient(HttpServletRequest request) {
		if (securityProvider != null) {
			return securityProvider.getHttpClient(request);
		}
		return new DefaultHttpClient();
	}

	/**
	 * Override this method if you would like the proxy to deal with particular
	 * queries.
	 * 
	 * @param host
	 * @param url
	 * @param serviceRequestType
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processProxyManagedUrl(URL host, String url,
			String serviceRequestType, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

			response.setContentType(PropertiesUtil.getProperty(Constants.TYPE_TEXT_XML));
			ServletOutputStream out = response.getOutputStream();
			
			String fileName = getProxyManagedUrls().get(url);
			
			if (fileName != null || !"".endsWith(fileName)){
				String absolutePath = request.getSession().getServletContext().getRealPath(fileName);
				String wsdlTemplate = FileUtils.readFileToString(new File(absolutePath));
				out.print(String.format(wsdlTemplate, host, name));
				out.flush();
				out.close();
			}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProxyContextUrl() {
		return proxyContextUrl;
	}

	public void setProxyContextUrl(String proxyUrl) {
		if (!proxyUrl.startsWith("/")) {
			proxyUrl = "/" + proxyUrl;
		}
		if (!proxyUrl.startsWith(PROXY_PREFIX)) {
			proxyUrl = PROXY_PREFIX + proxyUrl;
		}
		this.proxyContextUrl = proxyUrl;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Map<String, String> getProxyManagedUrls() {
		return proxyManagedUrls;
	}

	public void setProxyManagedUrls(Map<String, String> proxyManagedUrls) {
		if (proxyManagedUrls == null) {
			proxyManagedUrls = new HashMap<String, String>();
		}
		this.proxyManagedUrls = proxyManagedUrls;
	}

	public HttpInterpreter getClientRequestInterpreter() {
		return clientRequestInterpreter;
	}

	public void setClientRequestInterpreter(
			HttpInterpreter clientRequestInterpreter) {
		this.clientRequestInterpreter = clientRequestInterpreter;
	}

	public HttpGenerator getServerRequestGenerator() {
		return serverRequestGenerator;
	}

	public void setServerRequestGenerator(HttpGenerator serverRequestGenerator) {
		this.serverRequestGenerator = serverRequestGenerator;
	}

	public HttpInterpreter getServerResponseInterpreter() {
		return serverResponseInterpreter;
	}

	public void setServerResponseInterpreter(
			HttpInterpreter serverResponseInterpreter) {
		this.serverResponseInterpreter = serverResponseInterpreter;
	}

	public HttpGenerator getClientResponseGenerator() {
		return clientResponseGenerator;
	}

	public void setClientResponseGenerator(HttpGenerator clientResponseGenerator) {
		this.clientResponseGenerator = clientResponseGenerator;
	}
	
	public ISecurityProvider getSecurityProvider() {
		return securityProvider;
	}

	public void setSecurityProvider(ISecurityProvider securityProvider) {
		this.securityProvider = securityProvider;
	}

	public String getServiceRequestType() {
		return serviceRequestType;
	}

	public void setServiceRequestType(String serviceRequestType) {
		this.serviceRequestType = serviceRequestType;
	}
}