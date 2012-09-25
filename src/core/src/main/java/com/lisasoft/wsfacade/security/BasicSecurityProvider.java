package com.lisasoft.wsfacade.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * <p>
 * BASIC authentication provider - adds authentication to the request to the
 * 'real' server that this proxy is passing through to. This provider does NOT
 * interrogate the request for BASIC authentication credentials rather they must
 * be set in the the applicationContext.xml file.
 * </p>
 * <p>
 * If the host and/or port are null they will be automatically set to
 * AuthScope.ANY_HOST or AuthScope.ANY_PORT.
 * </p>
 * 
 * @author jhudson
 * 
 */
public class BasicSecurityProvider implements ISecurityProvider {

	private String host;
	private Integer port;
	private String username;
	private String password;

	@Override
	public HttpClient getHttpClient(HttpServletRequest request) {
		DefaultHttpClient client = new DefaultHttpClient();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(getHost(), getPort()),
				new UsernamePasswordCredentials(getUsername(), getPassword()));

		return client;
	}

	public String getHost() {
		if (this.host == null) {
			return AuthScope.ANY_HOST;
		}
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		if (this.port == null) {
			return AuthScope.ANY_PORT;
		}
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
