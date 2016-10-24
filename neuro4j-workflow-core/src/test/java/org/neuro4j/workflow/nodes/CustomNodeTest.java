package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.UUID;

import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowProcessor;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CustomNodeTest {
	
	@Test
	public void testCustomNode(){
		
		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		
		CustomNode  customNode = new CustomNode("org.neuro4j.workflow.core.SystemOutBlock", name, uuid);

		String endName = UUID.randomUUID().toString();
		String endUuid = UUID.randomUUID().toString();
		
		EndNode end1 = new EndNode(endName, endUuid);
		
		Transition transition = new Transition();
		transition.setName(SWFConstants.NEXT_RELATION_NAME);
		transition.setToNode(end1);
		
		customNode.registerExit(transition);
		
		assertThat(customNode.getExitByName(SWFConstants.NEXT_RELATION_NAME), is(transition));
		assertThat(customNode.getExits(), IsCollectionContaining.hasItem(transition));
		
		assertEquals(name, customNode.getName());
		assertEquals(uuid, customNode.getUuid());
		
		assertEquals(endName, end1.getName());
		assertEquals(endUuid, end1.getUuid());
		
		try {
			customNode.init();
		} catch (FlowInitializationException e1) {
			fail(e1.getMessage());
		}

		
		try {
			customNode.validate(processor, new FlowContext());
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
		
		WorkflowRequest request = new WorkflowRequest();
		
		
		try {
			customNode.execute(processor, request);
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
		
		assertEquals(transition, request.getNextWorkflowNode());
	}
	
	
}
