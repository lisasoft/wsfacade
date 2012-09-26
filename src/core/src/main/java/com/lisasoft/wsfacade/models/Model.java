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
package com.lisasoft.wsfacade.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Interpreters populate a model to store details of a request to translate. 
 * @author jgroffen
 * @author jhudson
 */
public class Model {
	
	private Map<String, String> properties = new HashMap<String, String>();
	
	public Model(String modelPropertyNames) {
		for(String property : modelPropertyNames.split(",")) {
			String propertyName = property;
			String propertyValue = "";
			if (property.contains("=")){
				String[] propertyNameValue = property.split("=");
				propertyName = propertyNameValue[0];
				propertyValue = propertyNameValue[1];
			}
			getProperties().put(propertyName.trim(), propertyValue.trim());
		}
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
