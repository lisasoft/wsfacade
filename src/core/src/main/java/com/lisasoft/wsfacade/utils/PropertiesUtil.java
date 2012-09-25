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
package com.lisasoft.wsfacade.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {
	private static Properties properties = null;
	private static boolean triedAndFailed = false;
	private static final Logger log = Logger.getLogger(PropertiesUtil.class);

	private static void load() {
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
		} catch (IOException e) {
			triedAndFailed = true;
			log.error("An error occured trying to load the application properties: ",e);
		}
	}

	/**
	 * Get a property from any of the classpath*:application.properties files
	 * within the build product
	 * 
	 * @param key
	 *            the name of the property you are looking for
	 * @return the property as a string or null if not found or the properties
	 *         file(s) could not be loaded
	 */
	public static String getProperty(String key) {
		if (properties == null && !triedAndFailed) {
			load();
		}
		if (properties != null) {
			return properties.getProperty(key);
		}
		return null;
	}
}
