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
package com.lisasoft.wsfacade.wmts.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lisasoft.wsfacade.models.RestModel;
import com.lisasoft.wsfacade.utils.SoapConstants;

public class GetTileModel extends RestModel {
	public List<String> dimensionsOrder = new ArrayList<String>();
	public Map<String, String> dimensions = new HashMap<String, String>();
	
	public GetTileModel() {
		this(SoapConstants.GET_TILE_MODEL);
		order = SoapConstants.GET_TILE_ORDER.split(",");
	}

	public GetTileModel(String modelPropertyNames) {
		super(modelPropertyNames);
	}
}
