package org.neuro4j.workflow.common;

import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;

import static junit.framework.Assert.*;


public class Neuro4jWorkflowTest {
	
	@Test
	public void testCreateEngine() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader()));
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("End1", result.getLastSuccessfulNodeName());
	}
	
	@Test
	public void testCreateEngineWithMapCache() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader())
            		.withWorkflowCache(new ConcurrentMapWorkflowCache()));
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("End1", result.getLastSuccessfulNodeName());
            
            result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("End1", result.getLastSuccessfulNodeName());
            engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
	}


}
