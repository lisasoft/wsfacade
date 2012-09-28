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
package com.lisasoft.wsfacade.interpreters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;

import com.lisasoft.wsfacade.mappers.AbstractMapper;
import com.lisasoft.wsfacade.models.IModel;

/**
 * <p>
 * An interpreter class for KVP GET services. This object is responsible for the interpretation of requests in the form of KVP GET:
 * </p>
 * <p>
 * <b>http://HOST:PORT/service?property1=value1&...propertyN=valueN</b>
 * </p>
 * 
 * @author jhudson
 */
public class KvpInterpreter extends HttpInterpreter {

	private KvpInterpreter(AbstractMapper mapper) {
		super(mapper);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IModel interpretRequest(HttpServletRequest request) throws IOException {
		Enumeration<String> keys = request.getParameterNames();
		StringBuilder sb = new StringBuilder();
		while (keys.hasMoreElements()){
			String key = keys.nextElement();
			sb.append(key+"="+request.getParameter(key));
			sb.append(",");
		}
		return getMapper().mapToModel(request.getRequestURI(), sb.toString(), request.getContentType());
	}

	/**
	 * The response from a KVP Get request on an OGC service should be an XML body
	 */
	@Override
	public IModel interpretResponse(HttpResponse response) throws IOException {
		String textContent = null;
		byte[] binaryContent = null;
		IModel result = null;
		
		if(log.isDebugEnabled()) {
			log.debug("Content Type: " + response.getEntity().getContentType().getValue());
		}

		if(response.getEntity() == null) {
			throw new IOException("response has no content");
		} else {
			if(response.getEntity().getContentType().getValue().startsWith("text/")) {
				textContent = readString(response);
				if(log.isDebugEnabled()) {
					log.debug("Text Content: \n\n---\n" + textContent + "\n---\n\n");
				}
				result = getMapper().mapToModel(textContent, response.getEntity().getContentType().getValue());
			} else {
				binaryContent = readBinary(response);
				if(log.isDebugEnabled()) {
					log.debug("Binary Content length: " + binaryContent.length);
				}
				result = getMapper().mapToModel(binaryContent, response.getEntity().getContentType().getValue());
			}
		}
		return result;
		
	}
}
