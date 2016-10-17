package org.neuro4j.workflow.common;

import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;

import static junit.framework.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
	
	@Test
	public void testDefaultConstructorEngine() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine();
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("End1", result.getLastSuccessfulNodeName());
	}
	
	@Test
	public void testDefaultConstructorWithParameters() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine();
            Map<String, Object> parameters = new HashMap<String, Object>();
            String randomStr = UUID.randomUUID().toString();
            parameters.put("message", randomStr);
            
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", parameters);
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("Hi" + randomStr, result.getFlowContext().get("text"));
	}
	
	@Test
	public void testDefaultConstructorWithParametersAndWorkflowLoader() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine(new ClasspathWorkflowLoader());
            Map<String, Object> parameters = new HashMap<String, Object>();
            String randomStr = UUID.randomUUID().toString();
            parameters.put("message", randomStr);
            
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", parameters);
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("Hi" + randomStr, result.getFlowContext().get("text"));
	}
	
	@Test
	public void testDefaultConstructorWithEmptyParameters() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine(new ClasspathWorkflowLoader());
            Map<String, Object> parameters = new HashMap<String, Object>();
            
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", parameters);
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("Hinull", result.getFlowContext().get("text"));
	}
	
	@Test
	public void testDefaultConstructorWithNullParameters() throws FlowExecutionException{
            WorkflowEngine engine = new WorkflowEngine(new ClasspathWorkflowLoader());
            Map<String, Object> parameters = null;
            
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", parameters);
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("Hinull", result.getFlowContext().get("text"));
	}

	@Test
	public void testDefaultConstructorWithWorkflowRequest() throws FlowExecutionException{
            
		WorkflowEngine engine = new WorkflowEngine(new ClasspathWorkflowLoader());
            Map<String, Object> parameters = new HashMap<String, Object>();
            String randomStr = UUID.randomUUID().toString();
            parameters.put("message", randomStr);
            
            WorkflowRequest request = new WorkflowRequest(parameters);
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", request);
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("Hi" + randomStr, result.getFlowContext().get("text"));
	}
}
