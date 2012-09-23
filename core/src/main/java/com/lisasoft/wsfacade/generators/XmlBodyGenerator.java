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

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.mappers.Mapper;
import com.lisasoft.wsfacade.mappers.UnsupportedModelException;
import com.lisasoft.wsfacade.models.Model;
import com.lisasoft.wsfacade.utils.Constants;
import com.lisasoft.wsfacade.utils.PropertiesUtil;

public class XmlBodyGenerator extends HttpGenerator {

    static final Logger log = Logger.getLogger(XmlBodyGenerator.class);

	public XmlBodyGenerator(Mapper mapper) {
		super(mapper);
	}

	@Override
	public HttpRequestBase generateRequest(Model model, String url, String requestType) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException {
		// for now all REST requests are GETs.
		if(requestType.equals("get")) {
			return(generateGetRequest(model, url));
		} else {
			return(generatePostRequest(model, url));
		}
	}


	@Override
	protected HttpGet generateGetRequest(Model model, String url) throws UnsupportedModelException, URISyntaxException {
		HttpGet result = null;
		String xml = mapper.mapFromModel(model);
		
		result = new HttpGet(new URI(url));
		
		result.getParams().setParameter("xml", xml);
		
		return result;
	}

	@Override
	protected HttpPost generatePostRequest(Model model, String url) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException {
		return generatePostRequest(model, url, PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}
	protected HttpPost generatePostRequest(Model model, String url, String charset) throws UnsupportedEncodingException, UnsupportedModelException, URISyntaxException {
		HttpPost result = null;
		String xml = mapper.mapFromModel(model);

		result = new HttpPost(new URI(url));
		
		// TODO: Make the charset a default setting from context.xml
		HttpEntity entity = null;
		try {
			entity = new StringEntity(xml, charset);
		} catch (UnsupportedEncodingException e) {
			log.warn(String.format("Charset '%s' was unsupported. Defaulting to '%s'.", charset, PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET)));
			charset = PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET);
			try {
				entity = new StringEntity(xml, charset);
			} catch (UnsupportedEncodingException uee) {
				log.error(String.format("The default charset '%s' was unsupported.", PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET)), uee);
				throw uee;
			}
		}
		
		result.setEntity(entity);
		
		result.setHeader("Content-type", "text/xml; charset=" + charset);
		
		return result;
	}

	@Override
	public void generateResponse(Model model, HttpServletResponse response) throws IOException, UnsupportedModelException {
		generateResponse(model, response, PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}

	public void generateResponse(Model model, HttpServletResponse response, String charset) throws IOException, UnsupportedModelException {
		String xml = mapper.mapFromModel(model);
		
		response.setContentType("text/xml");
		ServletOutputStream out = response.getOutputStream();
		
		out.print(xml);
		out.flush();
		out.close();
	}
}
