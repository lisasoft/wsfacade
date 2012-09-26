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

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;

import com.lisasoft.wsfacade.mappers.EntityMapper;
import com.lisasoft.wsfacade.models.Model;

/**
 * Base interpreter class for Rest services.
 * @author jgroffen
 * @author jhudson
 *
 */
public class RestInterpreter extends HttpInterpreter {

	private RestInterpreter(EntityMapper mapper) {
		super(mapper);
	}

	@Override
	public Model interpretRequest(HttpServletRequest request) throws IOException {
		String textContent = null;
		if(request.getContentLength() > 0) {
			if(request.getContentType().startsWith("text/")) {
				textContent = readString(request); 
			}
		}
		
		return getMapper().mapToModel(request.getRequestURI(), request.getContentType(), textContent);
	}

	@Override
	public Model interpretResponse(HttpResponse response) throws IOException {
		String textContent = null;
		byte[] binaryContent = null;
		Model result = null;
		
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
