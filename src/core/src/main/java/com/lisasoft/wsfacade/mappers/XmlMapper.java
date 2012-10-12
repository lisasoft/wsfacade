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

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.UnsupportedModelException;
import com.lisasoft.wsfacade.models.XmlModel;

/**
 * Represents an XML model. literally holing the XML Node object.
 * @author jhudson
 *
 */
public class XmlMapper extends AbstractMapper {

	static final Logger log = Logger.getLogger(XmlMapper.class);

	protected String startTag = null;

	protected XmlMapper() {
	}

	protected XmlMapper(String startTag) {
		this.startTag = startTag;
	}

	/**
	 * The input here is likely to be a SOAP envelope
	 */
	public IModel mapToModel(String source) throws IllegalArgumentException {
		IModel result = null;

		Builder builder = new Builder();
		try {
			Document doc = builder.build(new ByteArrayInputStream(source.getBytes()));
			result = new XmlModel(doc.getRootElement());
		} catch (NullPointerException ex) {
		} catch (ParsingException ex) {
		} catch (IOException ex) {
		}

		return result;
	}

	public String mapFromModel(IModel model) throws UnsupportedModelException {
		return model.getTextSourceAsXML();
	}
}
