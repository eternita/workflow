package org.neuro4j.workflow.hystrix;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowEngine;

public class HystrixTest {
	


	@Before
	public void setUp() {
	
	
	}
	
	
	@Test
	public void testSympleHystryxCommand() {

		WorkflowEngine engine = new WorkflowEngine();
		Map<String, Object> parameters = new HashMap<String, Object>();

		String name = "Mister";
		
		parameters.put("name", name);

		ExecutionResult result = engine.execute("org.neuro4j.workflow.hystrix.FlowWithHystrix-StartNode1", parameters);
		assertNotNull(result);
		assertNull(result.getException());
		Map<String, Object> returnParameters = result.getFlowContext().getParameters();
		

		String message = (String)returnParameters.get("message");
		assertEquals("Hello, " + name, message);

	}
	
	@Test
	public void testSleepCommand() {

		WorkflowEngine engine = new WorkflowEngine();
		Map<String, Object> parameters = new HashMap<String, Object>();

		
		parameters.put("sleepMs", 1500l);

		ExecutionResult result = engine.execute("org.neuro4j.workflow.hystrix.FlowWithHystrix-StartNode2", parameters);
		assertNotNull(result);
		assertNull(result.getException());
		Map<String, Object> returnParameters = result.getFlowContext().getParameters();
		
		String lastNode = result.getLastSuccessfulNodeName();

		assertEquals("EndNode3", lastNode);
		
	}
	

}
