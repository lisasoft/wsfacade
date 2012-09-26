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
package com.lisasoft.wsfacade.interpreters;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lisasoft.wsfacade.utils.FileConfigurationContext;

/**
 * Test the KvpInterpreter
 * s
 * @author jhudson
 *
 */
public class KvpInterpreterTest {

	private KvpInterpreter interpreter;
	
	@Before
	public void setUp() throws Exception {
		interpreter = FileConfigurationContext.getInstance().getBean("kvpInterpreter", KvpInterpreter.class);
	}

	@After
	public void tearDown() throws Exception {
		interpreter = null;
	}

	@Test
	public void testMapper() {
		assertNotNull(interpreter.getMapper());
	}
}
