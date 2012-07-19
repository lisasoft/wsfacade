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
package com.lisasoft.wsfacade.generators;

import com.lisasoft.wsfacade.config.Translatable;
import com.lisasoft.wsfacade.mappers.Mapper;

public class GeneratorFactory {
	
	private GeneratorFactory() {
		// This class can't be instantiated.
	}
	
	public static HttpGenerator getInstance(Translatable type, Mapper mapper) throws IllegalArgumentException {
		HttpGenerator instance;
		
		if(type == Translatable.SOAP) {
			instance = new XmlBodyGenerator(mapper);
		} else if(type == Translatable.REST) {
			instance = new RestGenerator(mapper);
		} else {
			throw new IllegalArgumentException(String.format("No generators exist for type %s.", type));
		}
		
		return instance;
	}
}
