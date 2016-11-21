package org.neuro4j.workflow.fork;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.CounterWorkflowLoader;

public class ForkTest {
	
	WorkflowConverter converter;
	CounterWorkflowLoader loader;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	
	}
	
	@Test
	public void testCallWorkflowWithForkDefaultPath() {
		loader = new CounterWorkflowLoader(new ClasspathWorkflowLoader(converter));
		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(loader));
		Map<String, Object> parameters = new HashMap<String, Object>();
		String randomStr = UUID.randomUUID().toString();
		parameters.put("message", randomStr);

		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.ForkWorkflow-StartNode1", parameters);
		assertNotNull(result);
		assertNull(result.getException());
		Map<String, Object> returnParameters = result.getFlowContext().getParameters();
		assertThat(returnParameters, IsMapContaining.hasEntry("var1", "1"));
		assertThat(returnParameters, IsMapContaining.hasEntry("var2", "2"));
		assertThat(returnParameters, IsMapContaining.hasEntry("var3", "3"));
		assertThat(returnParameters, IsMapContaining.hasEntry("path", "1"));

		assertEquals(1, loader.getCounter().get());
	}
	
	@Test
	public void testCallWorkflowWithForkPath1() {
		loader = new CounterWorkflowLoader(new ClasspathWorkflowLoader(converter));
		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(loader));
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("path", "path1");

		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.ForkWorkflow-StartNode1", parameters);
		assertNotNull(result);
		assertNull(result.getException());
		Map<String, Object> returnParameters = result.getFlowContext().getParameters();
		assertThat(returnParameters, IsMapContaining.hasEntry("var1", "1"));
		assertThat(returnParameters, IsMapContaining.hasEntry("var2", "2"));
		assertThat(returnParameters, IsMapContaining.hasEntry("var3", "3"));
		assertThat(returnParameters, IsMapContaining.hasEntry("path", "2"));

		assertEquals(1, loader.getCounter().get());
	}
	
	
	@Test
	public void testCallWorkflowWithForkPath2() {
		loader = new CounterWorkflowLoader(new ClasspathWorkflowLoader(converter));
		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(loader));
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("path", "path2");

		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.ForkWorkflow-StartNode1", parameters);
		assertNotNull(result);
		assertNull(result.getException());
		Map<String, Object> returnParameters = result.getFlowContext().getParameters();
		assertThat(returnParameters, IsMapContaining.hasEntry("var1", "1"));
		assertThat(returnParameters, IsMapContaining.hasEntry("var2", "2"));
		assertThat(returnParameters, IsMapContaining.hasEntry("var3", "3"));
		
		// check main path
		assertThat(returnParameters, IsMapContaining.hasEntry("path", "3"));

		assertEquals(1, loader.getCounter().get());
	}
}
