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
package com.lisasoft.wsfacade.utils;

import java.io.File;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

/**
 * This objects responsibility is to load XML into an accessible object, This is
 * to start replacing the SOAPConstants.java String xml objects and load the xml
 * from .xml files from the resources directory.
 * 
 * @author jhudson
 * 
 */
public class XMLUtilities {

	/**
	 * The static instance of the object
	 */
	private static XMLUtilities instance;

	/**
	 * Private constructor - singleton object
	 */
	private XMLUtilities() {}

	public static XMLUtilities getInstance() {
		if (instance == null) {
			instance = new XMLUtilities();
		}
		return instance;
	}

	/**
	 * Load an XML file into a Document for later use
	 * 
	 * @param xmlFilePath
	 *            the full String path of the document to load
	 * @return a Document if the xml file can be read, null otherwise
	 */
	public Document loadDocument(final String xmlFilePath) {
		return loadDocument(new File(xmlFilePath));
	}

	/**
	 * 
	 * Load an XML file into a Document for later use
	 * 
	 * @param xmlFile
	 *            the File of the document to load
	 * @return a Document if the xml file can be read, null otherwise
	 */
	public Document loadDocument(final File xmlFile) {
		Document returnDoc = null;
		try {
			Builder parser = new Builder();
			returnDoc = parser.build(xmlFile);
		} catch (ParsingException ex) {
			System.err
					.println("Cafe con Leche is malformed today. How embarrassing!");
		} catch (IOException ex) {
			System.err
					.println("Could not connect to Cafe con Leche. The site may be down.");
		}
		return returnDoc;
	}
}