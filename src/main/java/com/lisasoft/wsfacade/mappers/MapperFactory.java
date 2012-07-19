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

import static com.lisasoft.wsfacade.config.ConfigKeys.MAPPINGS;
import static com.lisasoft.wsfacade.config.ConfigKeys.PROXIES;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.NamingException;

import com.lisasoft.wsfacade.config.Translatable;
import com.lisasoft.wsfacade.config.ConfigKeys.MappingKeys;
import com.lisasoft.wsfacade.proxies.Proxy;
import com.lisasoft.wsfacade.utils.ClassUtil;

public class MapperFactory {
	
	private MapperFactory() {
		// This class can't be instantiated.
	}
	
	public static Mapper getInstance(Proxy proxy, Translatable type, String translationSide, Context context) throws NamingException, IllegalArgumentException {
		Mapper instance;
		
		String prefix = String.format("%s.%s",PROXIES, proxy.getName());
		
		// has a custom mapper been set?
		String customMapper = (String)context.lookup(String.format("%s.%s.%s", prefix, translationSide, MappingKeys.CUSTOM_MAPPER));
		
		if(customMapper != null) {
			try {
				instance = (Mapper)ClassUtil.loadClassInstance(customMapper);
			} catch(Exception e) {
				throw new IllegalArgumentException(String.format("Couldn't create an instance of the custom mapper '%s'.", customMapper), e);
			}
		} else {
			// make an instance of a mapper based on the client / server type
			if(type == Translatable.SOAP) {
				instance = new SoapMapper();
			} else if(type == Translatable.REST) {
				instance = new RestMapper();
			} else {
				throw new IllegalArgumentException(String.format("No mappers exist for type %s.", type));
			}
		}
		
		instance.loadConfig(String.format("%s.%s", prefix, translationSide), context);
		
		return instance;
	}

	public static SortedSet<String> getAllMappingNames(String prefix, Context context) throws NamingException {

		SortedSet<String> mappings = new TreeSet<String>();

		String mappingsCommmaDelimited = (String) context.lookup(String.format("%s.%s", prefix, MAPPINGS));
		String[] stringArray = mappingsCommmaDelimited.split(",");
		mappings.addAll(Arrays.asList(stringArray));

		return mappings;
	}
}
