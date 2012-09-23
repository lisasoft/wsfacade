package com.lisasoft.wsfacade.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;

/**
 * The security provider for the proxy that you wish to use - configured in the
 * applicationContext.xml file this is a plug and play extension module for the
 * proxies.
 * 
 * @author jhudson
 * 
 */
public interface ISecurityProvider {
	public HttpClient getHttpClient(HttpServletRequest request);
}
