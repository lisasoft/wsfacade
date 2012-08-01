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

import com.lisasoft.wsfacade.models.Model;
import com.lisasoft.wsfacade.models.RestModel;

public class RestMapper extends EntityMapper {
	
    static final Logger log = Logger.getLogger(RestMapper.class);

    public Model mapToModel(String source) throws IllegalArgumentException {
		throw new IllegalArgumentException("Mapping from REST to the model is not supported yet.");
		// return null;
	}
	
	/**
	 * Return the REST path
	 * EG:
	 * {layer}/{style}/{firstDimension}/{...}/{lastDimension}/{TileMatrixSet}/{scale}/{TileRow}/{TileCol}.{format_extension}
	 */
    public String mapFromModel(Model model) throws UnsupportedModelException {
    	
    	StringBuffer result = new StringBuffer("");
		
		if(model instanceof RestModel) {
			RestModel m = (RestModel)model;
			
			for(String propertyName : m.order) {
				String property = m.properties.get(propertyName);
				
				if(property != null) {
					if(result.length() != 0) {
						result.append("/");
					}
					result.append(property);
				}
			}
		} else {
			throw new UnsupportedModelException(String.format("%s cannot map from model %s", getClass().getName(), model.getClass().getName()));
		}
			
		return result.toString();
	}
}
