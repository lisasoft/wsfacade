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
package com.lisasoft.wsfacade.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Interpreters populate a model to store details of a request to translate. 
 * @author jgroffen
 * @author jhudson
 */
public abstract class AbstractModel implements IModel {
	
	protected String contentType;
	protected boolean isBinary;
	protected byte[] binarySource = null;
	protected String textSource = null;
	protected Map<String, String> properties = new HashMap<String, String>();

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isBinary() {
		return isBinary;
	}

	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}

	public byte[] getBinarySource() {
		return binarySource;
	}

	public void setBinarySource(byte[] binarySource) {
		this.binarySource = binarySource;
	}

	public String getTextSource() {
		return textSource;
	}

	public void setTextSource(String textSource) {
		this.textSource = textSource;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
