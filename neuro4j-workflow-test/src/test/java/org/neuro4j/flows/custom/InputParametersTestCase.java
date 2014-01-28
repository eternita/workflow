package org.neuro4j.flows.custom;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.core.log.Logger;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;

public class InputParametersTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMandatoryParametersWithEmptyValue() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode1", params);			
			fail("");
			
		} catch (FlowExecutionException e) {
			
		}
	}

	@Test
	public void testMandatoryParametersWithMapping() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("var1", "123");
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode1", params);			
		
			
		} catch (FlowExecutionException e) {
			fail(e.getLocalizedMessage());
		}
	}
	@Test
	public void testMandatoryParametersWithDirectValue1() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mandatoryParameter", "123");
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode1", params);			
		
			fail("var1 should not be empty");
		} catch (FlowExecutionException e) {
			
		}
	}
	@Test
	public void testMandatoryParametersWithWrongType() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mandatoryParameter", new Integer(123));
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode2", params);			
		
			fail("Integer is wrong type. Should be String");
		} catch (FlowExecutionException e) {
			
		}
	}
	
	@Test
	public void testMandatoryParametersWithDirectValue2() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mandatoryParameter", "123");
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode2", params);			
		

		} catch (FlowExecutionException e) {
			fail(e.getLocalizedMessage());	
		}
	}
	
	
	/**
	 * if put "value1 " + "value2" as input parameter - it must concatenate it 
	 */
	@Test
	public void testConcatenation2StringForInputparameter() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode4", params);			
			
			String string2 = (String)logicContext.get("string2");
			
			Assert.assertEquals("value1 value2", string2);

		} catch (FlowExecutionException e) {
			fail(e.getLocalizedMessage());
			Logger.error(this, e.getLocalizedMessage(), e);
			
		}
	}
	
	/**
	 * put "value1 " + bean.name into input parameter
	 */
	@Test
	public void testConcatenationStringAndBeanForInputparameter() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			TestBean bean = new TestBean();
			bean.setStringVar("name1");
			params.put("var1", bean);
			
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.flows.custom.FlowWithParameters-StartNode5", params);			

			
			
			String string2 = (String)logicContext.get("string2");
			
			Assert.assertEquals("value1 name1", string2);

		} catch (FlowExecutionException e) {
			fail(e.getLocalizedMessage());
			Logger.error(this, e.getLocalizedMessage(), e);
			
		}
	}
}
