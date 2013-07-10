/**
 * 
 */
package org.neuro4j.flows.nodes.switchnode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.core.Entity;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 *
 */
public class SwitchNodeTestCase extends BaseFlowTestCase{


	@Test
	public void testRelationDynamic1() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("relationName", "rel1");
			
			Entity lastNode = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDynamic", params, "CURRENT_NODE");
			
			assertEquals(lastNode.getName(), "EndNode1");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRelationDynamic2() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("relationName", "rel2");
			
			Entity lastNode = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDynamic", params, "CURRENT_NODE");

			assertEquals(lastNode.getName(), "EndNode2");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
		
	@Test
	public void testRelationStatic1() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			Entity lastNode = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeStatic", params, "CURRENT_NODE");

			assertEquals(lastNode.getName(), "EndNode3");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRelationWrongConfiguration() {
		try {
			
			Entity lastNode = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDynamic", "CURRENT_NODE");


			fail("Should be exception.");

		} catch (FlowExecutionException e) {
			
		}
	}
	
	@Test
	public void testRelationDefaultExit() {
		try {

			Entity lastNode = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDefaultExit", "CURRENT_NODE");

			assertEquals(lastNode.getName(), "EndNode5");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	@Test
	public void testRelationEmptyExit() {
		try {
		
			Entity lastNode = (Entity) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeEmptyExit", "CURRENT_NODE");

			assertEquals(lastNode.getName(), "EndNode7");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
}
