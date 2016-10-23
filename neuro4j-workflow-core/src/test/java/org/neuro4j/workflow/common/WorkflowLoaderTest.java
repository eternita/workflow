package org.neuro4j.workflow.common;

import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.FileWorkflowLoader;

import static junit.framework.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.UUID;


public class WorkflowLoaderTest {
	WorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	}
	@Test
	public void testDefaultClassPathLoader() throws FlowExecutionException{
		ClasspathWorkflowLoader loader = new ClasspathWorkflowLoader(converter);
		Workflow workflowSource = loader.load("org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflowSource);
	}
	
	@Test
	public void testClassPathLoader() throws FlowExecutionException{
		ClasspathWorkflowLoader loader = new ClasspathWorkflowLoader(converter);
		Workflow flow = loader.load("org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(flow);

		assertEquals("org.neuro4j.workflow.flows.FlowForClasspathLoader", flow.getFlowName());
		assertEquals("org.neuro4j.workflow.flows", flow.getPackage());
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testClassPathLoaderNotFound() throws FlowExecutionException{
		ClasspathWorkflowLoader loader = new ClasspathWorkflowLoader(converter);
		loader.load("org.neuro4j.workflow.flows.SomeFlow");
	}
	

	@Test
	public void testDefaultFileWorkflowLoader() throws FlowExecutionException{
		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader(converter);
		
		File baseDir = getBaseDirectory();
		
		assertTrue(baseDir.exists());
		assertTrue(baseDir.isDirectory());
		FileWorkflowLoader loader = new FileWorkflowLoader(converter, classpathLoader, baseDir);
		Workflow workflowSource = loader.load("org.mydomain.FlowForFileWorkflowLoader");
		assertNotNull(workflowSource);
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultFileWorkflowLoaderWithEmptyBaseDirectory() throws FlowExecutionException {
		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader(converter);
		
		new FileWorkflowLoader(converter, classpathLoader, null);
        fail("Should be exception");
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultFileWorkflowLoaderWithFileAsBaseDirectory() throws FlowExecutionException {
		ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader(converter);
		
		File file = getTestFile();
		assertTrue(file.exists());
		
		new FileWorkflowLoader(converter, classpathLoader, file);
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
