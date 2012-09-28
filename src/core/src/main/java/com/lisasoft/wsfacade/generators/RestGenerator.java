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
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.mappers.IMapper;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.utils.Constants;
import com.lisasoft.wsfacade.utils.PropertiesUtil;

public class RestGenerator extends HttpGenerator {

    static final Logger log = Logger.getLogger(RestGenerator.class);

	public RestGenerator(IMapper mapper) {
		super(mapper);
	}

	@Override
	public HttpRequestBase generateRequest(IModel model, String url, String requestType) throws UnsupportedModelException, URISyntaxException {
		// for now all REST requests are GETs.
		return(generateGetRequest(model, url));
	}

	@Override
	protected HttpGet generateGetRequest(IModel model, String url) throws UnsupportedModelException, URISyntaxException {
		HttpGet result = null;
		String restRequest = getMapper().mapFromModel(model);

		try {
			log.debug(String.format("Request URI: %s", url + restRequest));
			result = new HttpGet(new URI(url + restRequest));
		} catch (URISyntaxException e) {
			throw new URISyntaxException("Generated REST URI failed", url + restRequest);
		}

		return result;
	}

	@Override
	protected HttpPost generatePostRequest(IModel model, String url) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException {
		return generatePostRequest(model, url, PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}

	public HttpPost generatePostRequest(IModel model, String url, String charset) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException {
		HttpPost result = null;
		
		String restRequest = getMapper().mapFromModel(model);
		try {
			log.debug(String.format("Request URI: %s", url + restRequest));
			result = new HttpPost(new URI(url + restRequest));
		} catch (URISyntaxException e) {
			throw new URISyntaxException("Generated REST URI failed", url + restRequest);
		}

		return result;
	}

	@Override
	public void generateResponse(IModel model, HttpServletResponse response) throws IOException, UnsupportedModelException {
		generateResponse(model, response, PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}

	public void generateResponse(IModel model, HttpServletResponse response, String charset) throws IOException, UnsupportedModelException {
		String xml = getMapper().mapFromModel(model);
		
		response.setContentType("text/xml");
		ServletOutputStream out = response.getOutputStream();
		out.print(xml);
		out.flush();
		out.close();
	}
}
