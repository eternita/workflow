package org.neuro4j.workflow.common;

import org.junit.Test;

import static junit.framework.Assert.*;

import java.io.File;
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
	

	@Test
	public void testDefaultFileWorkflowLoader() throws FlowExecutionException{
		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader();
		
		File baseDir = getBaseDirectory();
		
		assertTrue(baseDir.exists());
		assertTrue(baseDir.isDirectory());
		FileWorkflowLoader loader = new FileWorkflowLoader(classpathLoader, baseDir, ClasspathWorkflowLoader.DEFAULT_EXT);
		WorkflowSource workflowSource = loader.load("org.mydomain.FlowForFileWorkflowLoader");
		assertNotNull(workflowSource);
		Workflow flow = workflowSource.content();
		assertNotNull(flow);
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultFileWorkflowLoaderWithEmptyBaseDirectory() throws FlowExecutionException {
		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader();
		
		new FileWorkflowLoader(classpathLoader, null, ClasspathWorkflowLoader.DEFAULT_EXT);
        fail("Should be exception");
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultFileWorkflowLoaderWithFileAsBaseDirectory() throws FlowExecutionException {
		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader();
		
		File file = getTestFile();
		assertTrue(file.exists());
		
		new FileWorkflowLoader(classpathLoader, file, ClasspathWorkflowLoader.DEFAULT_EXT);
        fail("Should be exception");
	}
	
	File getBaseDirectory(){
		File someFile =  getTestFile();
		assertTrue(someFile.exists());
		File dir = someFile.getParentFile();
		assertTrue(dir.isDirectory());
		
		return new File(dir, "somefolder");
	}

	private File getTestFile() {
		URL stream = getClass().getClassLoader().getResource("file1.n4j");
		return new File(stream.getFile());
	}
	
}
