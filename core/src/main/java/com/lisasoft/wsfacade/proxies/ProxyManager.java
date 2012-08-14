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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.utils.ConfigurationContext;

/**
 * Used to get a translation instance appropriate to handle the given request based on configuration.
 * @author jgroffen
 * @author jhudson
 *
 */
public class ProxyManager {
	private static final Logger log = Logger.getLogger(ProxyManager.class);
	private static boolean config_loaded = false;
	private static List<Proxy> proxies = new ArrayList<Proxy>();
	
	public ProxyManager() throws ProxyException {
		if(!config_loaded) {
			loadConfig();
		}
	}

	private static void loadConfig() throws ProxyException {
		ConfigurationContext configurationContext = ConfigurationContext.getInstance();
		String[] proxyNames = configurationContext.getBeanNamesForType(Proxy.class);
		for (String proxyName : proxyNames) {
			log.debug(String.format("Loading Proxy Config: %s", proxyName));
			proxies.add(configurationContext.getBean(proxyName, Proxy.class));
		}
		config_loaded = true;
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

	public static List<Proxy> getProxies() throws ProxyException {
		if(!config_loaded) {
			loadConfig();
		}
		return proxies;
	}
}
