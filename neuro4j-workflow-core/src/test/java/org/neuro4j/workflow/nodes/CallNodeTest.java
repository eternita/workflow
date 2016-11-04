package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.CallNode;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowProcessor;

public class CallNodeTest {
	

	
	@Test
	public void testCallNodeWithWrongFlow() {
		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";

		CallNode callNode = new CallNode(name, uuid);

		try {
			callNode.validate(processor, new FlowContext());
			fail("Can not run call node without parameters");
		} catch (FlowExecutionException e) {
			assertEquals("CallNode node: Flow not defined.", e.getMessage());
		}

	}
	
	@Test
	public void testCallStaticFlow() {
		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		Map<String, Object> parameters = new HashMap<String, Object>();
		String randomStr = UUID.randomUUID().toString();
		parameters.put("message", randomStr);
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";

		CallNode callNode = new CallNode(name, uuid);
		callNode.setCallFlow("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2");
		try {
			callNode.execute(processor, new WorkflowRequest(parameters));
			
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCallDynamicFlow() {
		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("myflow", "org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2");
		String randomStr = UUID.randomUUID().toString();
		parameters.put("message", randomStr);
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";

		CallNode callNode = new CallNode(name, uuid);
		callNode.setDynamicFlownName("myflow");
		try {
			callNode.execute(processor, new WorkflowRequest(parameters));
			
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
	}
	
	
//	@Test
//	public void testCustomBlockWithWrongClass(){
//           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
//		
//		String name = UUID.randomUUID().toString() + "Name";
//		String uuid = UUID.randomUUID().toString() + "uuid";
//		String className = UUID.randomUUID().toString() + "Class";
//		
//		CustomNode  customNode = new CustomNode(className, name, uuid);
//		WorkflowRequest request = new WorkflowRequest();
//		
//		try {
//			customNode.execute(processor, request);
//			fail("Can not run custom node with null");
//		} catch (FlowExecutionException e) {
//              assertEquals("CustomBlock: " + className + " can not be initialized.", e.getMessage());
//		}
//		
//	}
//	
//	@Test
//	public void testCustomBlockClassNotImplementedActionBlock(){
//           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
//		
//		String name = UUID.randomUUID().toString() + "Name";
//		String uuid = UUID.randomUUID().toString() + "uuid";
//		String className = "java.lang.String";
//		
//		CustomNode  customNode = new CustomNode(className, name, uuid);
//		WorkflowRequest request = new WorkflowRequest();
//		
//		try {
//			customNode.execute(processor, request);
//			fail("Can not run custom node with null");
//		} catch (FlowExecutionException e) {
//              assertEquals(className + " does not implement org.neuro4j.workflow.ActionBlock", e.getMessage());
//		}
//	}
//	
//	@Test
//	public void testCustomBlockSystemOutBlocke(){
//           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
//		
//		String name = UUID.randomUUID().toString() + "Name";
//		String uuid = UUID.randomUUID().toString() + "uuid";
//		String className = "org.neuro4j.workflow.core.SystemOutBlock";
//		
//		CustomNode  customNode = new CustomNode(className, name, uuid);
//		WorkflowRequest request = new WorkflowRequest();
//		
//		try {
//			customNode.execute(processor, request);
//			fail();
//		} catch (FlowExecutionException e) {
//            assertEquals("Next transition can not be null", e.getMessage());
//			
//	    }
//	}
//	
	public void executeCallNode(final String callFlow, final String dynamicFlow){
		
		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		
		CallNode  callNode = new CallNode(name, uuid);
		callNode.setCallFlow(callFlow);
		callNode.setDynamicFlownName(dynamicFlow);
		
		String endName = UUID.randomUUID().toString();
		String endUuid = UUID.randomUUID().toString();
		
		EndNode end1 = new EndNode(endName, endUuid);
		
		Transition transition = new Transition();
		transition.setName(SWFConstants.NEXT_RELATION_NAME);
		transition.setToNode(end1);
		
		callNode.registerExit(transition);
		
		assertThat(callNode.getExitByName(SWFConstants.NEXT_RELATION_NAME), is(transition));
		assertThat(callNode.getExits(), IsCollectionContaining.hasItem(transition));
		
		assertEquals(name, callNode.getName());
		assertEquals(uuid, callNode.getUuid());
		
		assertEquals(endName, end1.getName());
		assertEquals(endUuid, end1.getUuid());
		
		try {
			callNode.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		
		try {
			callNode.validate(processor, new FlowContext());
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
		
		WorkflowRequest request = new WorkflowRequest();
		
		
		try {
			callNode.execute(processor, request);
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
		
		assertEquals(transition, request.getNextWorkflowNode());
	}
	
}
