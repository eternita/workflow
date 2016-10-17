package org.neuro4j.workflow.common;

import org.junit.Test;

import static junit.framework.Assert.*;

import java.io.InputStream;
import java.net.URL;


public class WorkflowLoaderTest {
	
	@Test
	public void testDefaultClassPathLoader() throws FlowExecutionException{
		ClasspathWorkflowLoader loader = new ClasspathWorkflowLoader();
		WorkflowSource workflowSource = loader.load("org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflowSource);
		Workflow flow = workflowSource.content();
		assertNotNull(flow);
	}
	
	@Test
	public void testClassPathLoader() throws FlowExecutionException{
		ClasspathWorkflowLoader loader = new ClasspathWorkflowLoader(".n4j");
		WorkflowSource workflowSource = loader.load("org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflowSource);
		Workflow flow = workflowSource.content();
		assertNotNull(flow);
		assertEquals("org.neuro4j.workflow.flows.FlowForClasspathLoader", flow.getFlowName());
		assertEquals("org.neuro4j.workflow.flows", flow.getPackage());
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testClassPathLoaderNotFound() throws FlowExecutionException{
		ClasspathWorkflowLoader loader = new ClasspathWorkflowLoader(".n4j");
		loader.load("org.neuro4j.workflow.flows.SomeFlow");
	}
	

//	@Test
//	public void testDefaultFileWorkflowLoader() throws FlowExecutionException{
//		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader();
//		URL stream = getClass().getResource("file1.n4j");
//		
//		FileWorkflowLoader loader = new FileWorkflowLoader(classpathLoader, "");
//		WorkflowSource workflowSource = loader.load("org.mydomain.FlowForFileWorkflowLoader");
//		assertNotNull(workflowSource);
//		Workflow flow = workflowSource.content();
//		assertNotNull(flow);
//	}
}
