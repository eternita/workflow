package org.neuro4j.flows.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.tests.base.BaseFlowTestCase;

public class NetworkTestCase extends BaseFlowTestCase {
	
	@Test
	public void testPublicNetworkPublicNode() {
		try {
			
			LogicContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode1");
			String var1 = (String)logicContext.get("value1");

			assertEquals(var1, "test");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}

	
	@Test
	public void testPublicNetworkPrivateNode() {
		try {
			
			LogicContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode2");

			fail("StartNode2 is private node");

		} catch (FlowExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCallPrivateNodeByCallNode() {
		try {
			
			LogicContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode3");
			String var1 = (String)logicContext.get("var1");

			assertEquals(var1, "test");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}

	
	@Test
	public void testCallPrivateNodeByCallNodeInOtherPackage() {
		try {
			
			executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode4");


			fail("Called private node has different package");

		} catch (FlowExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCallPublicNodeByCallNodeInOtherPackage() {
		try {
		
			LogicContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode5");
			String var1 = (String)logicContext.get("var1");
			assertEquals(var1, "test value");
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
}
