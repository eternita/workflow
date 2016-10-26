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
		
	executeCustomBlock("org.neuro4j.workflow.core.SystemOutBlock");
	}
	
	@Test
	public void testCustomBlockWithNoAnnotation(){
		executeCustomBlock("org.neuro4j.workflow.core.CustomBlockWithNoAnotations");
	}
	
	@Test
	public void testCustomBlockWithEmptyList(){
		executeCustomBlock("org.neuro4j.workflow.core.CustomBlockWithEmptyList");
	}
	
	@Test
	public void testCustomBlockWithNull(){
           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		
		CustomNode  customNode = new CustomNode(null, name, uuid);
		WorkflowRequest request = new WorkflowRequest();
		
		try {
			customNode.execute(processor, request);
			fail("Can not run custom node with null");
		} catch (FlowExecutionException e) {
              assertEquals("ExecutableClass not defined for CustomNode " + name, e.getMessage());
		}
		
	}
	
	@Test
	public void testCustomBlockWithWrongClass(){
           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		String className = UUID.randomUUID().toString() + "Class";
		
		CustomNode  customNode = new CustomNode(className, name, uuid);
		WorkflowRequest request = new WorkflowRequest();
		
		try {
			customNode.execute(processor, request);
			fail("Can not run custom node with null");
		} catch (FlowExecutionException e) {
              assertEquals("CustomBlock: " + className + " can not be initialized.", e.getMessage());
		}
		
	}
	
	@Test
	public void testCustomBlockClassNotImplementedActionBlock(){
           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		String className = "java.lang.String";
		
		CustomNode  customNode = new CustomNode(className, name, uuid);
		WorkflowRequest request = new WorkflowRequest();
		
		try {
			customNode.execute(processor, request);
			fail("Can not run custom node with null");
		} catch (FlowExecutionException e) {
              assertEquals(className + " does not implement org.neuro4j.workflow.ActionBlock", e.getMessage());
		}
	}
	
	@Test
	public void testCustomBlockSystemOutBlocke(){
           WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		String className = "org.neuro4j.workflow.core.SystemOutBlock";
		
		CustomNode  customNode = new CustomNode(className, name, uuid);
		WorkflowRequest request = new WorkflowRequest();
		
		try {
			customNode.execute(processor, request);
			fail();
		} catch (FlowExecutionException e) {
            assertEquals("Next transition can not be null", e.getMessage());
			
	    }
	}
	
	public void executeCustomBlock(final String nodeName){
		
		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		
		CustomNode  customNode = new CustomNode(nodeName, name, uuid);

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
