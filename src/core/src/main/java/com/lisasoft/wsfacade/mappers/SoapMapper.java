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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.models.XmlModel;
import com.lisasoft.wsfacade.utils.SoapConstants;
import com.lisasoft.wsfacade.utils.XmlUtilities;

public class SoapMapper extends AbstractMapper {

	static final Logger log = Logger.getLogger(SoapMapper.class);

	protected String startTag = null;

	protected SoapMapper() {
	}

	protected SoapMapper(String startTag) {
		this.startTag = startTag;
	}

	/**
	 * The input here is likely to be a SOAP envelope
	 */
	public IModel mapToModel(String source, String host) throws IllegalArgumentException {
		IModel result = null;

		/*
		 * extract the data we need from the soap envelope 
		 */
		Builder builder = new Builder();
		try {
			Document doc = builder.build(new ByteArrayInputStream(source.getBytes()));
			Element root = doc.getRootElement();
			Element xmlResponse = root.getFirstChildElement("Body","http://www.w3.org/2003/05/soap-envelope"); /*TODO: this needs to be dynamic for the correct soap namespace*/

			if (xmlResponse==null) {
				xmlResponse = root.getFirstChildElement("Body","http://schemas.xmlsoap.org/soap/envelope/"); /*TODO: this needs to be dynamic for the correct soap namespace*/
			}

			Elements children = xmlResponse.getChildElements();
			Element response = children.get(0);
			
			if (response.toXML().contains("<wfs:WFS_Capabilities")){
				Element wsdl = new Element("wfs:WSDL", "http://www.opengis.net/wfs/2.0");
				wsdl.addAttribute(new Attribute("xlink:href", "http://www.w3.org/1999/xlink", host+"?WSDL"));
				response.appendChild(wsdl);
			}
			result = new XmlModel(response.copy());
		} catch (NullPointerException ex) {
		} catch (ParsingException ex) {
		} catch (IOException ex) {
		}
		
		return result;
	}

	public String mapFromModel(IModel model) throws UnsupportedModelException {
		String result = null;
		if (model.getProperties().containsKey("response")) {
			// TODO: This error reporting needs standards conforming information
			// from the server so it can be properly reported here.
			result = String.format(SoapConstants.ERROR_RESPONSE_TEMPLATE,
					"OperationNotSupported",
					model.getProperties().get("response"));
		} else {
			/*
			 * Map the model into a standard OGC Soap request
			 */
			String soapTemplate = null;
			try {
				soapTemplate = XmlUtilities
						.loadDocument("standard_soap_template.xml");
			} catch (IOException e) {
				log.error("An error occured trying to load the SOAP template");
			}

			/*
			 * Here we need to map the model into an OGC XML part, which will
			 * then be added to the SOAP BODY element.
			 */
			String xml = model.getTextSourceAsXML();

			/*
			 * Add the OGC XMl to the soap binding
			 */
			if (xml != null){
				result = String.format(soapTemplate, xml);
			}
		}

		if (log.isDebugEnabled()) {
			log.info(result);
		}

		return result;
	}
}
