package com.lisasoft.wsfacade.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lisasoft.wsfacade.interpreters.KvpInterpreter;
import com.lisasoft.wsfacade.models.IModel;
import com.lisasoft.wsfacade.utils.FileConfigurationContext;

public class KvpMapperTest {

	private static final String SERVICE_WMS = "service=wms,version=1.3.0,request=GetCapabilities";
	private static final String SERVICE_WMS_R = "request=GetCapabilities&service=wms&version=1.3.0";

	private KvpInterpreter interpreter;

	@Before
	public void setUp() throws Exception {
		interpreter = FileConfigurationContext.getInstance().getBean(
				"kvpInterpreter", KvpInterpreter.class);
	}

	@After
	public void tearDown() throws Exception {
		interpreter = null;
	}

	@Test
	public void testMapToModelString() {
		IModel model = interpreter.getMapper().mapToModel(SERVICE_WMS);
		assertEquals(model.getProperties().get("service"), "wms");
		assertEquals(model.getProperties().get("version"), "1.3.0");
		assertEquals(model.getProperties().get("request"), "GetCapabilities");
	}

	@Test
	public void testMapFromModel() throws Exception {
		IModel model = interpreter.getMapper().mapToModel(SERVICE_WMS);
		String testString = interpreter.getMapper().mapFromModel(model);
		assertEquals(SERVICE_WMS_R, testString);
	}
}