package org.neuro4j.workflow.common;

import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.loader.f4j.WorkflowBuilder;
import org.neuro4j.workflow.node.StartNode;
import static org.neuro4j.workflow.loader.f4j.WorkflowBuilder.*;


import static junit.framework.Assert.*;



public class WorkflowBuilderTest {


	WorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	}
	
	@Test
	public void testCreateAndRunWorkflow() throws FlowExecutionException{

		WorkflowBuilder builder = new WorkflowBuilder("org.neuro4j.TestWorkflow1", "StartNode1");;
		
		NodeXML lastNode = builder.getCurrentNode();
		assertNotNull(lastNode);
		String startNodeName = lastNode.getName();
		assertEquals("StartNode1", startNodeName);
		builder.addNext(createJoinNode());
		builder.addNext(createEndNode());
		
		lastNode = builder.getCurrentNode();
		assertNotNull(lastNode);
		
		String generatedEndNodeName = lastNode.getName();
		
		Workflow workflow =  builder.build();
		
		
		assertEquals("org.neuro4j.TestWorkflow1", workflow.getFlowName());
		assertEquals("org.neuro4j", workflow.getPackage());
		
		StartNode startNode2 = workflow.getStartNode(startNodeName);
		
		assertNotNull(startNode2);
		assertEquals(startNodeName, startNode2.getName());
		assertNotNull(startNode2.getUuid());
		
		
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
		ExecutionResult result = engine.execute(workflow, startNodeName, new WorkflowRequest());	
		assertNotNull(result);
		assertNull(result.getException());
		assertEquals(generatedEndNodeName, result.getLastSuccessfulNodeName());
	}
	
	/**
	 * Create StartNode1 -> JoinNode -> EndNode
	 * 
	 * @throws FlowExecutionException
	 */
	@Test
	public void testCreateWorkflowShort() throws FlowExecutionException{

		WorkflowBuilder builder = new WorkflowBuilder("org.neuro4j.TestWorkflow1", "StartNode1");;

		builder.addNext(createJoinNode());
		builder.addNext(createEndNode());
		
		Workflow workflow =  builder.build();
		
		
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
		ExecutionResult result = engine.execute(workflow, "StartNode1", new WorkflowRequest());	
		assertNotNull(result);
		assertNull(result.getException());
	}
	
	/**
	 * Create StartNode2 -> CustomNode(o.n.w.c.SystemOutBlock) ->JoinNode -> EndNode
	 *                         |- onError-> EndNode; 
	 *
	 *                          
	 * @throws FlowExecutionException
	 */
	@Test
	public void testCreateWorkflowWithCustomNode() throws FlowExecutionException{

		Workflow workflow = new WorkflowBuilder("org.neuro4j.TestWorkflow1", "StartNode2") // creates workflow and StartNode
				                .addCustomNode("org.neuro4j.workflow.core.SystemOutBlock")
				                    // add mapping name -> varToPrint
				                   .withInputParam("varToPrint", "name")
				                      .withOnError(createEndNode()).done()
				                .addJoinNode()
				                .build(); // creates EndNode if does not exist

		
		// running workflow
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
		
		WorkflowRequest request = new WorkflowRequest();
	
		request.addParameter("name", "Hello!!!");
		
		ExecutionResult result = engine.execute(workflow, "StartNode2", request);	
		assertNotNull(result);
		assertNull(result.getException());
	}
	
}
