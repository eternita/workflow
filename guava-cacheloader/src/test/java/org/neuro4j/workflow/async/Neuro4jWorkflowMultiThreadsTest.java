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
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.guava.GuavaWorkflowCache;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neuro4jWorkflowMultiThreadsTest {
	private static final Logger logger = LoggerFactory.getLogger(Neuro4jWorkflowMultiThreadsTest.class);
	WorkflowEngine engine;
	CounterXmlWorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new CounterXmlWorkflowConverter();
		engine = new WorkflowEngine(new ConfigBuilder().withWorkflowCache(GuavaWorkflowCache.cacheWithExpiration(10, TimeUnit.SECONDS)).withLoader(new ClasspathWorkflowLoader(converter)));
	}


	@Test
	public void testRunMultyThreads() throws FlowExecutionException {

		ExecutorService executor = Executors.newFixedThreadPool(5);

		String[] urls = new String[]{"org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode3",
			                         "org.neuro4j.workflow.flows.FlowForClasspathLoaderOtherFile-StartNode3"};
		
		
		
		for (int i = 0; i < 20; i++) {
			int index = ThreadLocalRandom.current().nextInt(0, 2);
			
			Runnable worker = new MyRunnable(urls[index]);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {

		}
		assertEquals(2, converter.getCounter().get());
		logger.debug("\nFinished all threads");

	}

	public class MyRunnable implements Runnable {
		private final String flow;

		MyRunnable(String flow) {
			this.flow = flow;
		}

		@Override
		public void run() {
			String name = UUID.randomUUID().toString();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name1", name);
			parameters.put("timeToSlee", ThreadLocalRandom.current().nextInt(10, 800 + 1));

			ExecutionResult executionResult = engine.execute(flow, parameters);

			assertNotNull(executionResult);
			assertNull(executionResult.getException());
			assertEquals("Hi " + name, executionResult.getFlowContext().get("value1"));
			assertEquals(name, executionResult.getFlowContext().get("name1"));
		}
	}
	
	public static class CounterXmlWorkflowConverter implements WorkflowConverter{
		
		XmlWorkflowConverter converter = new XmlWorkflowConverter();
		
		AtomicInteger counter = new AtomicInteger(0);
	

		@Override
		public Workflow convert(Reader reader, String name) throws FlowExecutionException {
			counter.incrementAndGet();
			return converter.convert(reader, name);
		}

		@Override
		public String getFileExt() {
			return converter.getFileExt();
		}

		public AtomicInteger getCounter() {
			return counter;
		}
		
		
	}
}
