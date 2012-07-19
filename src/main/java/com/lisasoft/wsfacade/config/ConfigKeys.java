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
package com.lisasoft.wsfacade.config;

/**
 * Class contains the sections of capability context configuration keys and
 * values. Keys are derived by concatenating various key segments together
 * separated by the period character '.'
 * 
 * @author mvivian
 * @author jgroffen
 * 
 */
public class ConfigKeys {
	/**
	 * Prefix for all proxies.
	 * 
	 * <pre>
	 * Keys after:
	 *  - a proxy name  (eg <b>proxies</b>.<u>proxyname</u>.client.type)
	 * On its own:
	 *  - Returns a list of all proxy names configured in context
	 * </pre>
	 */
	public static String PROXIES = "proxies";

	/**
	 * Prefix for all mappings provided by a proxy.
	 * 
	 * <pre>
	 * Keys before: 
	 *  - proxy name    (eg proxies.<u>wmts_soap2rest</u>.<b>mappings</b>
	 * Keys after:
	 *  - a mapping name  (eg proxies.wmts_soap2rest.<b>mappings</b>.<u>get_tile</u>)
	 * On its own:
	 *  - Returns a list of all mappings for a proxy
	 * </pre>
	 */
	public static String MAPPINGS = "mappings";

	/**
	 * Prefix for proxy settings that pertain to the client
	 * 
	 * <pre>
	 * Keys before: 
	 *  - proxy name      (eg proxies.<u>wmts_soap2rest</u>.<b>client</b>
	 * Keys after:
	 *  - client settings (eg proxies.wmts_soap2rest.<b>client</b>.<u>type</u>)
	 * </pre>
	 * 
	 * TranslationKeys has a CLIENT setting as well.
	 */
	public static String CLIENT = "client";

	/**
	 * Prefix for proxy settings that pertain to the server
	 * 
	 * <pre>
	 * Keys before: 
	 *  - proxy name      (eg proxies.<u>wmts_soap2rest</u>.<b>server</b>
	 * Keys after:
	 *  - server settings (eg proxies.wmts_soap2rest.<b>server</b>.<u>type</u>)
	 * </pre>
	 * 
	 * TranslationKeys has a SERVER setting as well.
	 */
	public static String SERVER = "server";

	/**
	 * Postfix for the value of a layers type definition
	 * 
	 * <pre>
	 * Keys before: 
	 *  - client or server  (eg proxies.wmts_soap2rest.<u>client</u>.<b>type</b>)
	 * Keys after:
	 *  - None
	 * </pre>
	 */
	public static String TYPE = "type";

	/**
	 * Postfix for URL of the proxy or the proxied service.
	 * 
	 * <pre>
	 * Keys before: 
	 *  - a proxy name  (eg proxies.<u>wmts_soap2rest</u>.<b>url</b>)
	 *  - SERVER        (eg proxies.wmts_soap2rest.<u>server</u>.<b>url</b>)
	 * Keys after:
	 *  - None
	 * </pre>
	 */
	public static String URL = "url";

	/**
	 * Postfix for a list of proxy managed URLS that don't get passed on to the
	 * translator.
	 * 
	 * <pre>
	 * Keys before: 
	 *  - a proxy name  (eg proxies.<u>wmts_soap2rest</u>.<b>proxy_managed</b>)
	 * Keys after:
	 *  - None
	 * </pre>
	 */
	public static String PROXY_MANAGED_URLS = "proxy_managed";

	/**
	 * Postfix for the fully qualified class name of a class that extends Proxy
	 * to use.
	 * 
	 * <pre>
	 * Keys before: 
	 *  - a proxy name  (eg proxies.<u>wmts_soap2rest</u>.<b>custom_proxy</b>)
	 * Keys after:
	 *  - None
	 * </pre>
	 */
	public static String CUSTOM_PROXY = "custom_proxy";

	// config keys for connection proxy details. This is if the SIP
	// service needs to use a proxy service to be able to access the
	// services to interpret.
	public static String CONNECTION_PROXY = "connection_proxy";
	public static String ENABLED = "enabled";
	public static String PORT = "port";
	public static String USERNAME = "username";
	public static String PASSWORD = "password";

	/**
	 * Postfix keys for translation specific configurations
	 * 
	 * @author jgroffen
	 */
	public static class MappingKeys {

		/**
		 * Postfix key for a comma separated list of possible properties in the
		 * model of a translation.
		 * 
		 * <pre>
		 * Keys before: 
		 *  - translation name (eg proxies.wmts_soap2rest.translations.<u>get_tile</u>.<b>model</b>)
		 * Keys after:
		 *  - None
		 * </pre>
		 */
		public static String MODEL = "model";

		/**
		 * Prefix for proxy settings that pertain to the client
		 * 
		 * <pre>
		 * Keys before: 
		 *  - translation name (eg proxies.wmts_soap2rest.translations.<u>get_tile</u>.<b>client</b>)
		 * Keys after:
		 *  - client settings for this translation (eg proxies.wmts_soap2rest.translations.get_tile.<b>client</b>.<u>start_tag</u>)
		 * </pre>
		 */
		public static String CLIENT = "client";

		/**
		 * Prefix for proxy settings that pertain to the server
		 * 
		 * <pre>
		 * Keys before: 
		 *  - translation name                     (eg proxies.wmts_soap2rest.translations.<u>get_tile</u>.<b>server</b>)
		 * Keys after:
		 *  - server settings for this translation (eg proxies.wmts_soap2rest.<b>server</b>.<u>order</u>)
		 * </pre>
		 */
		public static String SERVER = "server";

		/**
		 * The name of a class that will perform custom mapping for a
		 * translation.
		 * 
		 * <pre>
		 * Keys before: 
		 *  - client or server (eg proxies.wmts_soap2rest.translations.get_tile.<u>client</u>.<b>custom_mapping</b>)
		 * Keys after:
		 *  - none
		 * </pre>
		 */
		public static String CUSTOM_MAPPER = "custom_mapper";

		/**
		 * A comma separated list of the parameter names to be mapped that
		 * specified the expected order. Needed for REST clients/servers.
		 * 
		 * <pre>
		 * Keys before: 
		 *  - client or server (eg proxies.wmts_soap2rest.translations.get_tile.<u>server</u>.<b>order</b>)
		 * Keys after:
		 *  - none
		 * </pre>
		 */
		public static String ORDER = "order";

		/**
		 * Postfix keys for translation configurations specific to a translation
		 * server or client type.
		 * 
		 * @author jgroffen
		 */
		public static class TranslationSoapClientKeys {
			/**
			 * Fully qualified name of a class to perform mapping of SOAP
			 * requests or responses to a model.
			 */
			public static String CUSTOM_MAPPER = "custom_mapper";
		}

		public static class TranslationRestServerKeys {
			/**
			 * Order to place parameters onto the REST URL in.
			 */
			public static String ORDER = "order";
		}
	}
}
