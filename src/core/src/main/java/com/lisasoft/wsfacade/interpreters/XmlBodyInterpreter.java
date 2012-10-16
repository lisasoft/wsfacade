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

import com.lisasoft.wsfacade.mappers.AbstractMapper;
import com.lisasoft.wsfacade.models.IModel;

/**
 * Base interpreter class where the body of the HTTP message is text/xml.
 * @author jgroffen
 *
 */
public class XmlBodyInterpreter extends HttpInterpreter {

	public XmlBodyInterpreter(AbstractMapper mapper) {
		super(mapper);
	}

	@Override
	public IModel interpretRequest(HttpServletRequest request, String host) throws IOException {
		String xml = readString(request);
		return getMapper().mapToModel(xml, host);
	}

	@Override
	public IModel interpretResponse(HttpResponse response, String host) throws IOException {
		String xml = readString(response);
		return getMapper().mapToModel(xml, host);
	}

}
