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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.models.KvpModel;

public class XMLUtilitiesTest {

	String xmlFileName = "wfs_GetFeature.xml";

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testObjects() {
		assertNotNull(xmlFileName);
	}

	@Test
	public void testloadDocument() throws IOException {
		assertNotNull(XmlUtilities.loadDocument(xmlFileName));
	}

	@Test
	public void testmanipulateDocument() throws IOException {
		String xmlDoc = XmlUtilities.loadDocument(xmlFileName);
		assertNotNull(xmlDoc);
	}

	@Test
	public void testMapModelToOGCXML(){
		IModel model = new KvpModel("request=getfeature,typename=tds:AircraftHangarGeopoint,service=wfs,version=2.0.0,maxfeatures=10");
		String xml = null;
		try {
			xml = XmlUtilities.mapModelToOGCXML(model);
		} catch (IOException ioe) {
			fail();
		}
		assertNotNull(xml);
	}
}