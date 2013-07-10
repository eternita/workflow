/**
 * 
 */
package org.neuro4j.flows.nodes.decisionnode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.core.Entity;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 * TODO: add tests
 */
public class DecisionNodeTestCase extends BaseFlowTestCase{



	@Test
	public void testStringEqualsTrue() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("var1", "123");
			params.put("var2", "123");
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEqStr", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode1");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testStringEqualsFalse() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("var1", "123");
			params.put("var2", "1234");
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEqStr", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode2");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testStringEqualsWithInteger() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("var1", "123");
			params.put("var2",  new Integer("123"));
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEqStr", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode2");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testDefinedTrue() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var1", "123");
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeDefined", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode3");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testDefinedFalse() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeDefined", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode4");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testUndefinedTrue() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeUndefined", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode5");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testUndefinedFalse() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var1", "123");
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeUndefined", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode6");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testEmptyStringTrue() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var1", "");
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode7");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testEmptyStringFalse() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var1", "123");
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode8");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testEmptyStringNull() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var1", null);
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode8");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testEmptyStringNotString() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var1", new Integer(123));
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode8");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	@Test
	public void testHasElementsArrayList() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<String> array = new ArrayList<String>();
			array.add("123");
			params.put("var1", array);
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params, "CURRENT_NODE");
			assertEquals(lastValue.getName(), "EndNode9");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	@Test
	public void testHasElementsArrayListEmpty() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<String> array = new ArrayList<String>();
			params.put("var1", array);
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode10");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	@Test
	public void testHasElementsStringArray() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String[] array = new String[]{"123", "12"};
			
			params.put("var1", array);
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode9");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testHasElementsStringArrayEmpty() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String[] array = new String[]{};
			
			params.put("var1", array);
			
			Entity lastValue = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params, "CURRENT_NODE");

			assertEquals(lastValue.getName(), "EndNode10");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
}
