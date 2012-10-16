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
package com.lisasoft.wsfacade.mappers;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.KvpModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;

/**
 * A URL mapper for KVP Get in the for:
 * 
 * http://HOST:PORT/service?property1=value1&...propertyN=valueN
 * 
 * @author jhudson
 */
public class KvpMapper extends AbstractMapper {

	static final Logger log = Logger.getLogger(KvpMapper.class);

	/**
	 * <p>
	 * This method is overridden for the request from a client to map all of its
	 * properties so they can generically be interpreted (mapped) into a new
	 * type of request.
	 * </p>
	 * <p>
	 * This method expects a String in the form:
	 * <b>http://HOST:PORT/service?property1=value1&...propertyN=valueN</b>
	 * </p>
	 */
	public IModel mapToModel(String source, String host) throws IllegalArgumentException {
		if (log.isDebugEnabled()){
			log.info("Parsing input string in the KvpMapper: '" + source  + "'");
		}

		return new KvpModel(source);
	}

	/**
	 * Return the KVP Get path EG: property1=value1&...propertyN=valueN
	 */
	public String mapFromModel(IModel model) throws UnsupportedModelException {

		StringBuffer result = new StringBuffer("");

		for (String propertyName : model.getProperties().keySet()) {
			String propertyValue = model.getProperties().get(propertyName);

			if (propertyName != null) {
				if (result.length() != 0) {
					result.append("&");
				}
				result.append(propertyName + "=" + propertyValue);
			}
		}

		return result.toString();
	}
}
