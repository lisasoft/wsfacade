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
package com.lisasoft.wsfacade.wmts.mappers;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.mappers.RestMapper;
import com.lisasoft.wsfacade.models.AbstractModel;
import com.lisasoft.wsfacade.models.CommonModel;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.utils.SoapConstants;
import com.lisasoft.wsfacade.wmts.models.GetTileModel;
import com.lisasoft.wsfacade.wmts.models.GetTileResponseModel;

public class WmtsRestMapper extends RestMapper {
	
    static final Logger log = Logger.getLogger(WmtsRestMapper.class);
    
    public IModel mapToModel(String source) throws IllegalArgumentException {
    	// This method has to return an entity model, as this mapper
    	// inherits from EntityMapper 
    	// a rest response with a sting source could be a getFeatureInfo response 
    	// or it could be a failed response.
    	IModel model = null;
		
    	if(source == null) {
    		model = new GetTileResponseModel(SoapConstants.GET_TILE_RESPONSE_MODEL);
		
    	} else if(source.contains("<Capabilities")) {
			// map to a get capabilities model
    		model = new CommonModel(SoapConstants.GET_CAPABILITIES_MODEL);
    		model.getProperties().put("capabilities", source);
		} else {
			model = new CommonModel("response");
			model.getProperties().put("response", source);
    	}
		
		return(model);
	}
	
	/**
	 * Return the REST path
	 * EG:
	 * {layer}/{style}/{firstDimension}/{...}/{lastDimension}/{TileMatrixSet}/{TileMatrix}/{TileRow}/{TileCol}.{format_extension}
	 * or
	 * {layer}/{style}/{firstDimension}/{...}/{lastDimension}/{TileMatrixSet}/{TileMatrix}/{TileRow}/{TileCol}/{J}/{I}.{InfoFormat_extension}
	 */
    public String mapFromModel(AbstractModel model) throws UnsupportedModelException {
    	
    	StringBuffer result = new StringBuffer("");
		
    	if(model instanceof GetTileModel) {
    		// this will also pick up a GetFeatureInfoModel instance, as it extends GetFeatureInfo
    		GetTileModel m = (GetTileModel)model;
    		
    		for(String propertyName : m.order) {
    			String property = m.getProperties().get(propertyName);
    			
    			if(propertyName.equals("InfoFormat")) {
    				if(property.contains(".")) {
    					property = property.substring(property.lastIndexOf(".") + 1);
    				}
    			}
    			
    			if(propertyName.equals("dimensions")) {
    				if(m.dimensions.size() > 0) {
    					for(String dimensionName : m.dimensionsOrder) {
    						if(m.dimensions.containsKey(dimensionName)) {
    							result.append("/").append(m.dimensions.get(dimensionName));
    						}
    					}
    				}
    			} else {
    				if(property != null && property.length() > 0) {
    					if(result.length() != 0) {
    						result.append("/");
    					}
    					result.append(property);
    				}
    			}
    		}
    		
    		result.replace(result.lastIndexOf("/"), result.lastIndexOf("/") + 1, ".");

		} else {
			if(model.getProperties().containsKey("name")) {
				if(model.getProperties().get("name").equals("GetCapabilities")) {
					result.append(model.getProperties().get("version"));
					result.append("/");
					result.append("WMTSCapabilities.xml");
				} else {
					throw new UnsupportedModelException(String.format("%s cannot map from model %s. Model with the property 'name' of '%s' not supported.", getClass().getName(), model.getClass().getName(), model.getProperties().get("name")));
				}
			} else {
				throw new UnsupportedModelException(String.format("%s cannot map from model %s. Model not supported or does not have expected properties.", getClass().getName(), model.getClass().getName()));
			}
		}
			
		return result.toString();
	}
}
