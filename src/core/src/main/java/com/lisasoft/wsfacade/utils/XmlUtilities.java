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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.lisasoft.wsfacade.models.IModel;

/**
 * This objects responsibility is to manipulate XML into an accessible object.
 * This is to start transition of the SOAPConstants.java Strings xml objects and
 * load the xml from .xml files from the resources directory.
 * 
 * @author jhudson
 * 
 */
public class XmlUtilities {

	static final Logger log = Logger.getLogger(XmlUtilities.class);

	/**
	 * Private constructor - singleton object
	 */
	private XmlUtilities() {
	}

	/**
	 * Load an XML template file into a String. This is use to string replace
	 * for request/response
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String loadDocument(final String fileName) throws IOException {
		File file = new File("xml", fileName);

		if (log.isDebugEnabled()) {
			log.info("Looking for file: " + file.getAbsolutePath());
		}

		/*
		 * Look for the file in the target dir - we might be testing check
		 */
		if (!file.exists()) {
			file = new File("target//test-classes//xml", fileName);

			if (log.isDebugEnabled()) {
				log.info("File doesnt exist, testing? looking for file: "
						+ file.getAbsolutePath());
			}
		}

		/*
		 * Look for the file in the test-harness dir - we might be testing check
		 * locally.
		 */
		if (!file.exists()) {
			file = new File("target//wsfacade-testharness//xml", fileName);

			if (log.isDebugEnabled()) {
				log.info("File doesnt exist, testing? looking for file: "
						+ file.getAbsolutePath());
			}
		}

		if (!file.exists()) {
			throw new FileNotFoundException("The file with name '"
					+ file.getAbsolutePath() + "' could not be found");
		}

		return FileUtils.readFileToString(file, PropertiesUtil.getProperty(Constants.DEFAULT_CHARSET));
	}

	/**
	 * <p>
	 * This method takes the key/value paris ard turns it into the correct OGC
	 * XML to be embedded into a SOAP:Body element.
	 * </p>
	 * 
	 * @param model
	 *            a model containing a key value pair Map representing an OGC
	 *            request
	 * @return String XML of the model representing the correct OGC XML request
	 * @throws IOException 
	 */
	public static String mapModelToOGCXML(IModel model) throws IOException {

		String request = model.getProperties().get("request");
		String version = model.getProperties().get("version");
		String service = model.getProperties().get("service");
		String outputFormat = model.getProperties().get("outputformat");
		String count = model.getProperties().get("count");
		String typeName = model.getProperties().get("typename");
		// String resultType = model.getProperties().get("resulttype");
		// String handle = model.getProperties().get("handle");
		// String featureId = model.getProperties().get("featureid");
		// String maxFeatures = model.getProperties().get("maxfeatures");
		// String fid = model.getProperties().get("featureid");

		String template = "";

		/*
		 * This will likely become a large unwieldy if statement and should be
		 * considered more
		 */
		if (service != null && "wfs".equals(service.toLowerCase())) {
			if (request != null && "getfeature".equals(request.toLowerCase())) {

				/*
				 * This template is really not great - it needs to take into
				 * consideration filters etc. proof of concept only.
				 */
				template = loadDocument("wfs_GetFeature.xml");

				return String
						.format(template,
								service,
								version,
								outputFormat == null ? "application/gml+xml; version=3.2"
										: outputFormat, count == null ? ""
										: count, typeName);
			}
		}

		return "";
	}
}