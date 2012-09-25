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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.config.Translatable;
import com.lisasoft.wsfacade.generators.HttpGenerator;
import com.lisasoft.wsfacade.interpreters.HttpInterpreter;
import com.lisasoft.wsfacade.mappers.Mapper;
import com.lisasoft.wsfacade.mappers.UnsupportedModelException;
import com.lisasoft.wsfacade.models.Model;
import com.lisasoft.wsfacade.security.ISecurityProvider;
import com.lisasoft.wsfacade.utils.Constants;

/**
 * A Proxy object which is loaded from the application context through spring
 * bindings.
 * 
 * @author jgroffen
 * @author jhudson
 */
public class Proxy {
	private static final Logger log = Logger.getLogger(Proxy.class);
	public static final String PROXY_PREFIX = "/proxies";

	protected String name = null;
	protected String proxyUrl = null;
	protected Translatable clientType = null;
	protected Translatable serverType = null;
	protected String serverUrl = null;
	private List<String> proxyManagedUrls = null;

	//protected Mapper clientMapper = null;
	//protected Mapper serverMapper = null;
	protected ISecurityProvider securityProvider = null;

	private HttpInterpreter clientRequestInterpreter = null;
	private HttpGenerator serverRequestGenerator = null;
	private HttpInterpreter serverResponseInterpreter = null;
	private HttpGenerator clientResponseGenerator = null;

	public void processRequest(String serviceRequestType,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		URL host = new URL("http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath() + proxyUrl);

		if (clientRequestInterpreter == null) {
			throw new ServletException(
					"Failed to load translation details: clientRequestInterpreter was null");
		} else if (serverRequestGenerator == null) {
			throw new ServletException(
					"Failed to load translation details: serverRequestGenerator was null");
		} else if (serverResponseInterpreter == null) {
			throw new ServletException(
					"Failed to load translation details: serverResponseInterpreter was null");
		} else if (clientResponseGenerator == null) {
			throw new ServletException(
					"Failed to load translation details: clientResponseGenerator was null");
		}

		if (log.isDebugEnabled()) {
			log.debug(String.format("Request for Proxy (%s)", name));
		}

		// remove the proxyUrl, we only take what was after the proxy path and
		// the query string.
		String requestedUrl = ("/proxies" + request.getPathInfo()).replace(
				proxyUrl, "") + "?" + request.getQueryString();
		if (getProxyManagedUrls().contains(requestedUrl)) {
			processProxyManagedUrl(host, requestedUrl, serviceRequestType,
					request, response);
		} else {
			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 1: Interpret Client Request (%s, %s)",
						clientRequestInterpreter.getClass().getName()));
						//clientMapper.getClass().getName()));
			}
			Model model = clientRequestInterpreter.interpretRequest(request);
			model.getProperties().put(Constants.HOST, host.toString());

			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 2: Generate Service Request (%s, %s)",
						serverRequestGenerator.getClass().getName()));
						//serverMapper.getClass().getName()));
			}

			HttpRequestBase serverRequest = null;

			// Exceptions at this level should only be thrown for situations the
			// interpreters / generators can't handle.
			try {
				serverRequest = serverRequestGenerator.generateRequest(model,
						serverUrl, serviceRequestType);
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

			HttpClient httpClient = getHttpClient(request);
			HttpResponse serverResponse = httpClient.execute(serverRequest);

			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 3: Interpret Server Response (%s, %s)",
						serverResponseInterpreter.getClass().getName()));
						//serverMapper.getClass().getName()));
			}
			model = serverResponseInterpreter.interpretResponse(serverResponse);
			model.getProperties().put("host", host.toString());

			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Step 4: Generate Client Response (%s, %s)",
						clientResponseGenerator.getClass().getName()));
						//clientMapper.getClass().getName()));
			}

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
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProxyUrl() {
		return proxyUrl;
	}

	public void setProxyUrl(String proxyUrl) {
		if (!proxyUrl.startsWith("/")) {
			proxyUrl = "/" + proxyUrl;
		}
		if (!proxyUrl.startsWith(PROXY_PREFIX)) {
			proxyUrl = PROXY_PREFIX + proxyUrl;
		}
		this.proxyUrl = proxyUrl;
	}

	public Translatable getClientType() {
		return clientType;
	}

	public void setClientType(Translatable clientType) {
		this.clientType = clientType;
	}

	public Translatable getServerType() {
		return serverType;
	}

	public void setServerType(Translatable serverType) {
		this.serverType = serverType;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public List<String> getProxyManagedUrls() {
		return proxyManagedUrls;
	}

	public void setProxyManagedUrls(List<String> proxyManagedUrls) {
		if (proxyManagedUrls == null) {
			proxyManagedUrls = new ArrayList<String>();
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
}