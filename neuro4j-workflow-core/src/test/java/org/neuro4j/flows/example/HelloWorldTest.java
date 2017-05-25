package org.neuro4j.flows.example;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;
import static org.neuro4j.workflow.loader.f4j.WorkflowBuilder.createEndNode;

import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.InMemoryWorkflowLoader;
import org.neuro4j.workflow.loader.f4j.WorkflowBuilder;

public class HelloWorldTest {
	
	WorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	}
	
	/**
	 * Create StartNode1 -> CustomNode(o.n.f.e.HelloWorld) ->JoinNode -> EndNode
	 *                         |- onError-> EndNode; 
	 *
	 *                          
	 * @throws FlowExecutionException
	 */
	@Test
	public void testCreateHalloWorldWorkflow() throws FlowExecutionException{

		Workflow workflow = new WorkflowBuilder("org.neuro4j.flows.HelloWorld", "StartNode1") // creates workflow and StartNode
				                .addCustomNode("org.neuro4j.flows.example.HelloWorld")
				                    // add mapping "name -> somename"
				                   .withInputParam("name", "somename")
				                   .withOutputParam("message", "text")
				                      .withOnError(createEndNode()).done()
				                .addJoinNode() // just for example
				                .build(); // builder creates EndNode by default if does not exist

		
		// running workflow
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
		
		WorkflowRequest request = new WorkflowRequest();
	
		request.addParameter("somename", "Mister");
		
		ExecutionResult result = engine.execute(workflow, "StartNode1", request);	
		assertNotNull(result);
		assertNull(result.getException());
		System.out.println(result.getFlowContext().get("text"));
	}

	/**
	 * Create StartNode1 -> CustomNode(o.n.f.e.HelloWorld) ->JoinNode -> EndNode
	 *                         |- onError-> EndNode; 
	 *        
	 *        StartNode2 -> CallNode(o.n.f.e.HelloWorld-StartNode1) -> EndNode
	 *                          
	 * @throws FlowExecutionException
	 */
	@Test
	public void testCreateHalloWorldWorkflowWithCallNode() throws FlowExecutionException{

		Workflow workflow = new WorkflowBuilder("org.neuro4j.flows.HelloWorld", "StartNode1") // creates workflow and StartNode
				                .addCustomNode("org.neuro4j.flows.example.HelloWorld")
				                    // add mapping "name -> somename"
				                   .withInputParam("name", "somename")
				                   .withOutputParam("message", "text")
				                      .withOnError(createEndNode()).done()
				                 .addEndNode()
				                 // add new flow with start node StartNode2
				             .addStartNode("StartNode2")
		                        .addCallNode("org.neuro4j.flows.HelloWorld-StartNode1")
		                     .build();
		
		
		InMemoryWorkflowLoader loader = new InMemoryWorkflowLoader(new ClasspathWorkflowLoader(converter));
		loader.register(workflow);
		
		// running workflow
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(loader));
		
		WorkflowRequest request = new WorkflowRequest();
	
		request.addParameter("somename", "Mister1");
		
		ExecutionResult result = engine.execute("org.neuro4j.flows.HelloWorld-StartNode2", request);	
		assertNotNull(result);
		assertNull(result.getException());
		assertEquals("Hello World! Mister1",result.getFlowContext().get("text"));
	}
	
	/**
	 * Create StartNode1 -> CustomNode(o.n.f.e.HelloWorld) -> EndNode
	 *
	 *                          
	 * @throws FlowExecutionException
	 */
	@Test
	public void testCreateHalloWorldWorkflowShort() throws FlowExecutionException{

		Workflow workflow = new WorkflowBuilder("org.neuro4j.flows.HelloWorld", "StartNode1")
				                .addCustomNode("org.neuro4j.flows.example.HelloWorld")
				                      .withOnError(createEndNode()).done()
				                .addEndNode()      
				                .build();

		
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder());
		
		WorkflowRequest request = new WorkflowRequest();
		request.addParameter("name", "Mister");
		
		ExecutionResult result = engine.execute(workflow, "StartNode1", request);
		assertEquals("Hello World! Mister", result.getFlowContext().get("message"));
	}

	
}
