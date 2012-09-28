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

import java.io.File;

import nu.xom.Comment;
import nu.xom.DocType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ProcessingInstruction;
import nu.xom.Text;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XMLUtilitiesTest {

	XMLUtilities instance;
	String xmlFilePath = "target\\classes\\xml\\FeatureInfoResponse.xml";
	File xmlFile;

	@Before
	public void setUp() throws Exception {
		instance = XMLUtilities.getInstance();
		xmlFile = new File(xmlFilePath);
	}

	@After
	public void tearDown() throws Exception {
		instance = null;
	}

	@Test
	public void testObjects() {
		assertNotNull(instance);
		assertNotNull(xmlFilePath);
		assertNotNull(xmlFile);
	}

	@Test
	public void testloadStringDocument() {
		assertNotNull(instance.loadDocument(xmlFile));
	}

	@Test
	public void testloadDocument() {
		assertNotNull(instance.loadDocument(xmlFilePath));
	}

	@Test
	public void testmanipulateDocument() {
		Document xmlDoc = instance.loadDocument(xmlFilePath);
		assertNotNull(xmlDoc);
		listChildren(xmlDoc.getRootElement(), 0);
	}

	public static void listChildren(Node current, int depth) {
		printSpaces(depth);
		String data = "";
		if (current instanceof Element) {
			Element temp = (Element) current;
			data = ": " + temp.getQualifiedName();
		} else if (current instanceof ProcessingInstruction) {
			ProcessingInstruction temp = (ProcessingInstruction) current;
			data = ": " + temp.getTarget();
		} else if (current instanceof DocType) {
			DocType temp = (DocType) current;
			data = ": " + temp.getRootElementName();
		} else if (current instanceof Text || current instanceof Comment) {
			String value = current.getValue();
			value = value.replace('\n', ' ').trim();
			if (value.length() <= 20)
				data = ": " + value;
			else
				data = ": " + current.getValue().substring(0, 17) + "...";
		}
		System.out.println(current.getClass().getName() + data);
		for (int i = 0; i < current.getChildCount(); i++) {
			listChildren(current.getChild(i), depth + 1);
		}
	}

	private static void printSpaces(int n) {
		for (int i = 0; i < n; i++) {
			System.out.print(' ');
		}
	}
}