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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.mappers.IMapper;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.utils.Constants;
import com.lisasoft.wsfacade.utils.PropertiesUtil;

public class XmlBodyGenerator extends HttpGenerator {

	static final Logger log = Logger.getLogger(XmlBodyGenerator.class);

	public XmlBodyGenerator(IMapper mapper) {
		super(mapper);
	}

	@Override
	public HttpRequestBase generateRequest(IModel model, String url,
			String requestType) throws UnsupportedEncodingException,
			UnsupportedModelException, URISyntaxException {
		if (requestType.equals("get")) {
			return (generateGetRequest(model, url));
		} else {
			return (generatePostRequest(model, url));
		}
	}

	@Override
	protected HttpGet generateGetRequest(IModel model, String url)
			throws UnsupportedModelException, URISyntaxException {
		HttpGet result = null;
		String xml = getMapper().mapFromModel(model);

		result = new HttpGet(new URI(url));

		result.getParams().setParameter("xml", xml);

		return result;
	}

	@Override
	protected HttpPost generatePostRequest(IModel model, String url)
			throws UnsupportedEncodingException, UnsupportedModelException,
			URISyntaxException {
		return generatePostRequest(model, url,
				PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}

	protected HttpPost generatePostRequest(IModel model, String url,
			String charset) throws UnsupportedEncodingException,
			UnsupportedModelException, URISyntaxException {
		HttpPost postMethod = null;
		String xml = getMapper().mapFromModel(model);

		postMethod = new HttpPost(new URI(url));

		HttpEntity entity = null;
		try {
			entity = new StringEntity(xml);
		} catch (UnsupportedEncodingException e) {
			log.warn(String.format(
					"Charset '%s' was unsupported. Defaulting to '%s'.",
					charset,
					PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET)));
			charset = PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET);
			try {
				entity = new StringEntity(xml, charset);
			} catch (UnsupportedEncodingException uee) {
				log.error(String.format(
						"The default charset '%s' was unsupported.",
						PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET)),
						uee);
				throw uee;
			}
		}

		postMethod.setEntity(entity);
		postMethod.setHeader("Content-type", "application/soap+xml; charset="
				+ charset);

		return postMethod;
	}

	@Override
	public void generateResponse(IModel model, HttpServletResponse response)
			throws IOException, UnsupportedModelException {
		generateResponse(model, response,
				PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}

	public void generateResponse(IModel model, HttpServletResponse response,
			String charset) throws IOException, UnsupportedModelException {
		String xml = getMapper().mapFromModel(model);
		
		if (!xml.startsWith("<?xml")) { /*If the XML header is missing, add it*/
			xml = "<?xml version=\"1.0\" encoding=\""+PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET)+"\"?>" + xml;
		}
		
		OutputStream out = response.getOutputStream();
		response.setContentType("text/xml");
		Document doc = null;
		try {
			Builder builder = new Builder();
			doc = builder.build(new ByteArrayInputStream(xml.getBytes()));
		} catch (ParsingException ex) {
			log.error(
					"An error occured trying to create an XML Document from the input String: ",
					ex);
		}
		Serializer serializer = null;
		try {
			serializer = new Serializer(out, "UTF-8");
			serializer.setIndent(4);
			serializer.setMaxLength(64);
			serializer.write(doc);
		} catch (IOException ex) {
			System.err.println(ex);
		} finally {
			if (serializer != null) {
				serializer.flush();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
}
