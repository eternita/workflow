package org.neuro4j.workflow.common;

import static junit.framework.Assert.*;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.ActionHandler;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.core.SystemOutBlock;
import org.neuro4j.workflow.node.WorkflowNode.NodeInfo;

public class ActionRegistryTest {
	
	static final String[] SYSOUT_IDS = { "lWbAqAIGEH8AAAFX8tCUseCZ", "ID3AqAIGnU8AAAFXeLM6OuCs" };
	static final String[] SYSOUT_NAMES = { "SystemOutBlock1", "SystemOutBlock2" };

	@Test
	public void testActionHandler() throws FlowExecutionException {

		AtomicInteger counterPreExecute = new AtomicInteger(0);
		AtomicInteger counterpostExecute = new AtomicInteger(0);
		
		ActionHandler handler = new ActionHandler(){
			
			@Override
			public  void preExecute(NodeInfo nodeInfo, FlowContext context, ActionBlock actionBlock){
				counterPreExecute.incrementAndGet();
				assertThat(SYSOUT_IDS, hasItemInArray(nodeInfo.getUuid()));
				assertThat(SYSOUT_NAMES, hasItemInArray(nodeInfo.getName()));
				if ("lWbAqAIGEH8AAAFX8tCUseCZ".equals(nodeInfo.getUuid())){
					assertEquals(context.get("varToPrint"), "value1");
				} else if ("ID3AqAIGnU8AAAFXeLM6OuCs".equals(nodeInfo.getUuid())){
					assertEquals(context.get("varToPrint"), "value2");
				} else {
				    fail();
				}
			}
			
			@Override
			public  void postExecute(NodeInfo nodeInfo, FlowContext context, ActionBlock actionBlock){
				counterpostExecute.getAndIncrement();
				assertThat(SYSOUT_IDS, hasItemInArray(nodeInfo.getUuid()));
				assertThat(SYSOUT_NAMES, hasItemInArray(nodeInfo.getName()));
			}
		};
		
		Map<Class<? extends ActionBlock>, ActionHandler> map = new HashMap<>();
		
		map.put(SystemOutBlock.class, handler);
		
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withActionRegistry(new ActionHandlersRegistry(map)));
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("var1", "value1");
		parameters.put("var2", "value2");
		
		
		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForFileLoader-StartNode1", parameters);
		assertNotNull(result);
		assertNull(result.getException());
		assertEquals("EndNode1", result.getLastSuccessfulNodeName());
		assertEquals(2, counterpostExecute.get());
		assertEquals(2, counterPreExecute.get());
	}
	
	
}
