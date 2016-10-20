package org.neuro4j.workflow.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.ActionHandler;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.cache.ActionRegistry;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.core.SystemOutBlock;


public class ActionRegistryTest {
	
	@Test
	public void testActionHandler() throws FlowExecutionException {

		AtomicInteger counter = new AtomicInteger(0);

		ActionHandler handler = new ActionHandler() {

			public void preExecute(ActionBlock actionBlock, FlowContext context) {
				counter.incrementAndGet();
			}

			public void postExecute(ActionBlock actionBlock, FlowContext context) {

			}
		};
		
		Map<Class<? extends ActionBlock>, ActionHandler> map = new HashMap<>();
		
		map.put(SystemOutBlock.class, handler);
		
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withActionRegistry(new ActionRegistry(map)));
		
		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForFileLoader-StartNode1");
		assertNotNull(result);
		assertNull(result.getException());
		assertEquals("EndNode1", result.getLastSuccessfulNodeName());
		assertEquals(2, counter.get());
	}
	
	
}
