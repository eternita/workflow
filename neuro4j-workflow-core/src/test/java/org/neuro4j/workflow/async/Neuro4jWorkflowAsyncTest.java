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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;

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
			parameters.put("name1", "" + i);
			parameters.put("timeToSlee", ThreadLocalRandom.current().nextInt(10, 800 + 1));
			FutureTask<ExecutionResult> result = engine.executeAsync("org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode3", parameters);
			logger.debug("Got " + i + " FutureTask<ExecutionResult>");
			list.add(result);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
                    logger.error(e.getMessage());
			}

		}
		int received = 0;
		boolean errors = false;

		while (received < list.size() && !errors) {
			int j = 0;
			for (FutureTask<ExecutionResult> t1 : list) {

				if (t1.isDone()) {
					try {
						ExecutionResult executionResult = t1.get();
						assertNotNull(executionResult);
						assertNull(executionResult.getException());
						assertEquals("Hi " + j, executionResult.getFlowContext().get("value1"));
						assertEquals(""+j, executionResult.getFlowContext().get("name1"));
						assertEquals("EndNode3", executionResult.getLastSuccessfulNodeName());
					} catch (Exception e) {
						errors = true;
						logger.error(e.getMessage(), e);
						fail(e.getMessage());
					}
					received++;
				}
				j++;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				errors = true;
				;
			}
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
	
	
	
}
