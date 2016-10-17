package org.neuro4j.workflow.common;

import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;

import static junit.framework.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;


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
	public void testCreateEngineWithMapCache() throws FlowExecutionException {
		
		final AtomicInteger counter = new AtomicInteger(0);
		
		final ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader();
		
            WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new WorkflowLoader() {
				
				@Override
				public WorkflowSource load(String name) throws FlowExecutionException {
					counter.incrementAndGet();
					return classpathLoader.load(name);
				}
			})
            		.withWorkflowCache(new ConcurrentMapWorkflowCache()));
            
            
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("End1", result.getLastSuccessfulNodeName());
            
            result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertEquals("End1", result.getLastSuccessfulNodeName());
            engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            
            assertEquals(1, counter.get());
	}
	
	


}
