package org.neuro4j.workflow.async;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.FutureTask;

import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neuro4jWorkflowAsyncTest {
	private static final Logger logger = LoggerFactory.getLogger(Neuro4jWorkflowAsyncTest.class);
	
	WorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	}

	@Test
	public void testRunFlowAsync() throws FlowExecutionException {

		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
		Map<String, Object> parameters = new HashMap<String, Object>();
		String randomStr = UUID.randomUUID().toString();
		parameters.put("message", randomStr);

		FutureTask<ExecutionResult> result = engine
				.executeAsync("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", parameters);
		logger.debug("Got FutureTask<ExecutionResult>");
		assertNotNull(result);
		try {
			ExecutionResult executionResult = result.get();
			assertNotNull(executionResult);
			assertNull(executionResult.getException());
			assertEquals("Hi" + randomStr, executionResult.getFlowContext().get("text"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	@Test
	public void testRunMultyFlowAsync() throws FlowExecutionException {

		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withWorkflowCache(EmptyWorkflowCache.INSTANCE).withLoader(new ClasspathWorkflowLoader(converter)));
		
		List<FutureTask<ExecutionResult>> list = new ArrayList();
		
		for (int i = 0 ; i < 10 ; i++){
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("message", "" + i);
			FutureTask<ExecutionResult> result = engine.executeAsync("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2", parameters);
			logger.debug("Got " + i + " FutureTask<ExecutionResult>");
			list.add(result);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
                    logger.error(e.getMessage());
			}

		}
		
		int j = 0;
		for(FutureTask<ExecutionResult> result: list){
			
			try {
				ExecutionResult executionResult = result.get();
				assertNotNull(executionResult);
				assertNull(executionResult.getException());
				assertEquals("Hi" + j, executionResult.getFlowContext().get("text"));
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}	
			j++;
		}
	

	}

	@Test
	public void testRunFlowAsync2() throws FlowExecutionException {
		WorkflowEngine engine = new WorkflowEngine();
		FutureTask<ExecutionResult> result = engine.executeAsync("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
		logger.debug("Got FutureTask<ExecutionResult>");
		assertNotNull(result);
		try {
			ExecutionResult executionResult = result.get();
			assertNotNull(executionResult);
			assertNull(executionResult.getException());
			assertEquals("End1", executionResult.getLastSuccessfulNodeName());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
//	@Test
//	public void testCreateEngineWithMapCache() throws FlowExecutionException {
//
//		final AtomicInteger counter = new AtomicInteger(0);
//
//		final ClasspathWorkflowLoader classpathLoader = new ClasspathWorkflowLoader(converter);
//
//		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new WorkflowLoader() {
//
//			@Override
//			public Workflow load(String name) throws FlowExecutionException {
//				counter.incrementAndGet();
//				return classpathLoader.load(name);
//			}
//		}).withWorkflowCache(new ConcurrentMapWorkflowCache()));
//
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("End1", result.getLastSuccessfulNodeName());
//
//		result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
//		assertNotNull(result);
//		assertEquals("End1", result.getLastSuccessfulNodeName());
//		engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
//		engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
//		engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
//
//		assertEquals(1, counter.get());
//	}
//
//	@Test
//	public void testDefaultConstructorEngine() throws FlowExecutionException {
//		WorkflowEngine engine = new WorkflowEngine();
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("End1", result.getLastSuccessfulNodeName());
//	}
//
//	@Test
//	public void testDefaultConstructorWithParameters() throws FlowExecutionException {
//		WorkflowEngine engine = new WorkflowEngine();
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		String randomStr = UUID.randomUUID().toString();
//		parameters.put("message", randomStr);
//
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2",
//				parameters);
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("Hi" + randomStr, result.getFlowContext().get("text"));
//	}
//
//	@Test
//	public void testDefaultConstructorWithParametersAndWorkflowLoader() throws FlowExecutionException {
//		WorkflowEngine engine = new WorkflowEngine(
//				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		String randomStr = UUID.randomUUID().toString();
//		parameters.put("message", randomStr);
//
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2",
//				parameters);
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("Hi" + randomStr, result.getFlowContext().get("text"));
//	}
//
//	@Test
//	public void testDefaultConstructorWithEmptyParameters() throws FlowExecutionException {
//		WorkflowEngine engine = new WorkflowEngine(
//				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
//		Map<String, Object> parameters = new HashMap<String, Object>();
//
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2",
//				parameters);
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("Hinull", result.getFlowContext().get("text"));
//	}
//
//	@Test
//	public void testDefaultConstructorWithNullParameters() throws FlowExecutionException {
//		WorkflowEngine engine = new WorkflowEngine(
//				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
//		Map<String, Object> parameters = null;
//
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2",
//				parameters);
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("Hinull", result.getFlowContext().get("text"));
//	}
//
//	@Test
//	public void testDefaultConstructorWithWorkflowRequest() throws FlowExecutionException {
//
//		WorkflowEngine engine = new WorkflowEngine(
//				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		String randomStr = UUID.randomUUID().toString();
//		parameters.put("message", randomStr);
//
//		WorkflowRequest request = new WorkflowRequest(parameters);
//		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode2",
//				request);
//		assertNotNull(result);
//		assertNull(result.getException());
//		assertEquals("Hi" + randomStr, result.getFlowContext().get("text"));
//	}
}
