package org.neuro4j.workflow.common;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.neuro4j.workflow.async.ThreadPoolTaskExecutor.ThreadPoolTaskConfig;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.loader.FileWorkflowLoader;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;


public class ConfigBuilderTest {
	
	@Test
	public void testDefaultConfigBuilder() throws FlowExecutionException{
	    	ConfigBuilder builder = new ConfigBuilder();
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(DefaultCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	
	@Test
	public void testConfigBuilderWithLoader() throws FlowExecutionException{
		    WorkflowConverter converter = new XmlWorkflowConverter();
	    	ConfigBuilder builder = new ConfigBuilder().withLoader(new FileWorkflowLoader(converter, new ClasspathWorkflowLoader(converter), new  File("/tmp")));
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(DefaultCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(FileWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	
	@Test
	public void testConfigBuilderWithCache() throws FlowExecutionException{
	    	ConfigBuilder builder = new ConfigBuilder().withWorkflowCache(new ConcurrentMapWorkflowCache());
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(DefaultCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	@Test
	public void testConfigBuilderWithInitStrategy() throws FlowExecutionException{
	    	ConfigBuilder builder = new ConfigBuilder().withCustomBlockInitStrategy(new DefaultCustomBlockInitStrategy());
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(DefaultCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	
	@Test
	public void testConfigBuilderWithActionRegistry() throws FlowExecutionException{
	    	ConfigBuilder builder = new ConfigBuilder().withActionRegistry(new ActionHandlersRegistry(new HashMap<>()));
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(DefaultCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	
	@Test
	public void testConfigBuilderWithAliases() throws FlowExecutionException{
		    Map<String, String> map = new HashMap<>();
		    map.put("productList", "org.neuro4j.workflow.flows.FlowForClasspathLoader");
		    map.put("product", "org.neuro4j.workflow.flows.FlowForClasspathLoader");
	    	ConfigBuilder builder = new ConfigBuilder().withAliases(map);
	    	
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(DefaultCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	        assertThat(builder.getAliases().keySet(), IsCollectionWithSize.hasSize(2));
	        assertThat(builder.getThreadPoolTaskConfig(), instanceOf(ThreadPoolTaskConfig.class));
	}
	
	@Test
	public void testConfigBuilderWithWrongAliases(){
		    Map<String, String> map = new HashMap<>();
		    map.put("productList", "org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode1");
		    map.put("product", "org.neuro4j.workflow.flows.FlowForClasspathLoader-StartNode1");
		    map.put("product1", null);
	    	try {
				ConfigBuilder builder = new ConfigBuilder().withAliases(map);
				fail("Should be exception");
			} catch (FlowExecutionException e) {
               assertEquals("Request flow can not be null", e.getMessage());
			}
	    	
	}
	
}
