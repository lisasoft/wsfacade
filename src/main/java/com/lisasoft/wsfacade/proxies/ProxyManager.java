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

import static com.lisasoft.wsfacade.config.ConfigKeys.CUSTOM_PROXY;
import static com.lisasoft.wsfacade.config.ConfigKeys.PROXIES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.utils.ClassUtil;

/**
 * Used to get a translation instance appropriate to handle the given request based on configuration.
 * @author jgroffen
 *
 */
public class ProxyManager {
	private static final Logger log = Logger.getLogger(ProxyManager.class);
	
	private static Context context;
	private static boolean config_loaded = false;
	private static List<Proxy> proxies = new ArrayList<Proxy>();
	
	public ProxyManager() throws ProxyException {
		if(!config_loaded) {
			loadConfig();
		}
	}
	
	private static void loadConfig() throws ProxyException {
		try {
			context = new InitialContext();
			context = (Context) context.lookup("java:comp/env");
		} catch (NamingException ne) {
			throw new ProxyException("Problem getting context when reading configuration.", ne);
		}

		SortedSet<String> proxyNames = getAllProxyNames();

		for(String proxyName: proxyNames) {
			log.debug(String.format("Loading Proxy Config: %s", proxyName));
			Proxy proxy = getProxyInstance(context, proxyName);
			
			try {
				proxy.loadConfig(context);
				proxies.add(proxy);
			} catch (NamingException e) {
				throw new ProxyException(String.format("Problem reading configuration for proxy '%s'.", proxyName), e);
			}
		}
		config_loaded = true;
	}
	
	private static Proxy getProxyInstance(Context context, String name) throws ProxyException {
		Proxy proxy = null;
		String customProxy = null;
		
		// has a custom proxy been set?
		try {
			customProxy = (String)context.lookup(String.format("%s.%s.%s", PROXIES, name, CUSTOM_PROXY));
		} catch(NameNotFoundException nnfe) {
			// this just means the context setting wasn't set.
			customProxy = null;
		} catch(Exception e) {
			throw new ProxyException(e);
		}

		if(customProxy != null) {
			try {
				proxy = (Proxy)ClassUtil.loadClassInstance(customProxy);
			} catch(Exception e) {
				throw new ProxyException(String.format("Couldn't create an instance of the custom proxy '%s'.", customProxy), e);
			}
		} else {
			proxy = new Proxy();
		}

		proxy.setName(name);
		return proxy;
	}
	
	/**
	 * Based on the URL the query came in on, return the proxy.
	 * Requested URL's should start with a value that can be matched to the URL
	 * of a configured proxy.
	 * @param url
	 * @return
	 */
	public Proxy getProxy(String url) throws ProxyException {
		Proxy result = null;

		log.debug(String.format("Proxy Count: %d", proxies.size()));
		
		for(Proxy p : proxies) {
			log.debug(String.format("Proxy URL: %s", p.getProxyUrl()));
			if(url.startsWith(p.getProxyUrl())) {
				result = p;
				break;
			}
		}
		
		if(result == null) {
			throw new ProxyException(String.format("No proxy configured for URL %s", url));
		} else
			return(result);
	}

	private static SortedSet<String> getAllProxyNames() throws ProxyException {
		SortedSet<String> proxies = new TreeSet<String>();
		try {
			String proxiesCommmaDelimited = (String) context.lookup(PROXIES);
			String[] stringArray = proxiesCommmaDelimited.split(",");
			proxies.addAll(Arrays.asList(stringArray));
		} catch (NamingException e) {
			throw new ProxyException("Problem reading proxies configuration.");
		}
		return proxies;
	}

	public static List<Proxy> getProxies() throws ProxyException {

		if(!config_loaded) {
			loadConfig();
		}
		return proxies;
	}
}
