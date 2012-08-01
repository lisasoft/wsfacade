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
package com.lisasoft.wsfacade;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.proxies.Proxy;
import com.lisasoft.wsfacade.proxies.ProxyException;
import com.lisasoft.wsfacade.proxies.ProxyManager;

/**
 * Performs the interpretation between a client and a server. There are four
 * steps to interpretation: - Interpret from the client HTTP request to a common
 * model for request information. - Use the common model for request information
 * to generate and perform a server request. - Interpret the response from the
 * server to a common model for response information. - Use the common model for
 * response information to generate and send the client response.
 * 
 * @author jgroffen
 * @author jhudson
 */
public class Service extends HttpServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2246108497161215329L;

	private static final Logger log = Logger.getLogger(Service.class);

	protected ProxyManager pm = null;

	public String getServletInfo() {
		return "Service Interpreting Proxy";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest("get", request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest("post", request, response);
	}

	/**
	 * Translate incoming requests. WSF is configured to know about a set of
	 * URL's. It looks at the URL requested by the client, matching this to a
	 * configuration. If a known translation has been configured it will be
	 * used.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest(String requestServiceType,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			if (pm == null) {
				pm = new ProxyManager();
			}

			if (log.isDebugEnabled()) {
				log.debug("Incoming request: " + request.getRequestURI());
			}

			// proxy path is without the context portion, remove it.
			String proxyUrl = request.getRequestURI();
			if (proxyUrl.startsWith(request.getContextPath())) {
				proxyUrl = proxyUrl
						.substring(request.getContextPath().length());
			}

			Proxy proxy = pm.getProxy(proxyUrl);

			proxy.processRequest(requestServiceType, request, response);

		} catch (ProxyException te) {
			throw new ServletException("Couldn't perform translation", te);
		}
	}

}
