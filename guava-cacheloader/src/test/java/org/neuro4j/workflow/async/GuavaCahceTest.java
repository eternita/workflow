package org.neuro4j.workflow.async;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.async.Neuro4jWorkflowMultiThreadsTest.CounterXmlWorkflowConverter;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.guava.GuavaWorkflowCache;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuavaCahceTest {
	private static final Logger logger = LoggerFactory.getLogger(GuavaCahceTest.class);


	@Before
	public void setUp() {
	}


	@Test
	public void testGuavaWorkflowCahe() throws FlowExecutionException {
		GuavaWorkflowCache cache = GuavaWorkflowCache.cacheWithExpiration(10, TimeUnit.SECONDS);
		
		CounterXmlWorkflowConverter converter = new CounterXmlWorkflowConverter();
		
		WorkflowLoader loader = new ClasspathWorkflowLoader(converter);
		
		Workflow workflow =  cache.get(loader, "org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflow);
		workflow =  cache.get(loader, "org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflow);
		assertEquals(1, converter.getCounter().get());
		//remove from cache
		cache.clear("org.neuro4j.workflow.flows.FlowForClasspathLoader");
		// and load again
		workflow =  cache.get(loader, "org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflow);
		assertEquals(2, converter.getCounter().get());
		
		cache.clearAll();
		// and load again
		workflow =  cache.get(loader, "org.neuro4j.workflow.flows.FlowForClasspathLoader");
		assertNotNull(workflow);
		assertEquals(3, converter.getCounter().get());

	}
	
	@Test(expected=FlowExecutionException.class)
	public void testGuavaWorkflowCaheThrowException() throws FlowExecutionException {
		GuavaWorkflowCache cache = GuavaWorkflowCache.cacheWithExpiration(10, TimeUnit.SECONDS);

		WorkflowLoader loader = new ClasspathWorkflowLoader(new XmlWorkflowConverter());

		cache.get(loader, "org.neuro4j.workflow.flows.SomeFlow");

	}


}
