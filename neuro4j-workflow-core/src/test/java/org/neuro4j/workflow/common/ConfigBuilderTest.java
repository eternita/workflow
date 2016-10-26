package org.neuro4j.workflow.common;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
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
	        assertThat(builder.getWorkflowCache(), instanceOf(EmptyWorkflowCache.class));
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
	        assertThat(builder.getWorkflowCache(), instanceOf(EmptyWorkflowCache.class));
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
	        assertThat(builder.getWorkflowCache(), instanceOf(EmptyWorkflowCache.class));
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
	        assertThat(builder.getWorkflowCache(), instanceOf(EmptyWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	
}
