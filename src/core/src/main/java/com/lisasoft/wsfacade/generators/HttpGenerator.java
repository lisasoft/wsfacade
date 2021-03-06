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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.lisasoft.wsfacade.mappers.IMapper;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;

/**
 * Given a common model, generate a Http request or response.
 * 
 * @author jgroffen
 * @author jhudson
 */
public abstract class HttpGenerator {

    private IMapper mapper;
    
	HttpGenerator(IMapper mapper) {
    	this.mapper = mapper;
    }

    public abstract HttpRequestBase generateRequest(IModel model, String url, String requestType) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException;

    protected abstract HttpGet generateGetRequest(IModel model, String url) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException;

	protected abstract HttpPost generatePostRequest(IModel model, String url) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException;

	public abstract void generateResponse(IModel model, HttpServletResponse response) throws IOException, UnsupportedModelException;
	
    public IMapper getMapper() {
		return mapper;
	}

	public void setMapper(IMapper mapper) {
		this.mapper = mapper;
	}
}
