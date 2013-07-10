package org.neuro4j.flows.custom;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.logic.LogicContext;

public class LogicContextTestCase {
	TestBean bean;

	@Before
	public void setUp() throws Exception {
		bean = new TestBean();
		bean.setStringVar("Hello, Mister");
	}

	@After
	public void tearDown() throws Exception {
		bean = null;
	
	}

	@Test
	public void testStringValueInContext() {
		
		LogicContext context = new LogicContext();
		context.put("var1", bean);
		Object value = context.get("var1.stringVar");
		if (value == null)
		{
			fail("Value should not be null");
		}
		if (!(value instanceof String))
		{
			fail("Wrong type");
		}
		if (!value.equals("Hello, Mister"))
		{
			fail("Wrong type");
		}
	}
	
	@Test
	public void testWrongKeyInContext() {
		
		LogicContext context = new LogicContext();
		context.put("var1", bean);
		Object value = context.get("var1.s");
		if (value != null)
		{
			fail("Value should not be null");
		}
		value = context.get("var1.");
		if (value != null)
		{
			fail("Value should not be null");
		}
	}

}
