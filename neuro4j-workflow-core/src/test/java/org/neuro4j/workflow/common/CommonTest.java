package org.neuro4j.workflow.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.hamcrest.collection.IsArrayContaining;
import org.hamcrest.collection.IsArrayWithSize;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.enums.DecisionCompTypes;
import org.neuro4j.workflow.enums.FlowVisibility;
import org.neuro4j.workflow.enums.StartNodeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonTest.class);
	
	
	@Test
	public void testParameterDefinitionImpl(){
		
		String type = UUID.randomUUID().toString();
		String name = UUID.randomUUID().toString();
		
		ParameterDefinitionImpl param = new ParameterDefinitionImpl(name, type, true);
		assertEquals(param.isOptional(), true);
		assertEquals(type, param.type());
		assertEquals(ParameterDefinition.class, param.annotationType());
		assertEquals(name, param.name());
	}
	
	@Test
	public void testFlowVisibility(){
		
		assertEquals(FlowVisibility.Public, FlowVisibility.getDefault());
		assertEquals(FlowVisibility.Public, FlowVisibility.getByName("public"));
		assertEquals(FlowVisibility.Private, FlowVisibility.getByName("private"));
		assertNull(FlowVisibility.getByName(UUID.randomUUID().toString()));
		assertThat(FlowVisibility.types(), IsArrayContaining.hasItemInArray("Private"));
		assertThat(FlowVisibility.types(), IsArrayContaining.hasItemInArray("Public"));
	}
	
	@Test
	public void testStartNodeTypes(){
		
		assertEquals(StartNodeTypes.PUBLIC, StartNodeTypes.getDefaultType());
		assertEquals(StartNodeTypes.PUBLIC, StartNodeTypes.getByName("PUBLIC"));
		assertEquals(StartNodeTypes.PRIVATE, StartNodeTypes.getByName("PRIVATE"));
		assertNull(StartNodeTypes.getByName(UUID.randomUUID().toString()));
		assertThat(StartNodeTypes.types(), IsArrayContaining.hasItemInArray("Private"));
		assertThat(StartNodeTypes.types(), IsArrayContaining.hasItemInArray("Public"));
		assertEquals(StartNodeTypes.PUBLIC, StartNodeTypes.getByDisplayName("Public"));
		assertEquals(StartNodeTypes.PRIVATE, StartNodeTypes.getByDisplayName("Private"));
	}
	
	@Test
	public void testDecisionCompTypes(){
		
		assertThat(DecisionCompTypes.operators(), IsArrayWithSize.arrayWithSize(2));
		assertEquals(DecisionCompTypes.context.getDisplayName(), "value from context");
		assertEquals(DecisionCompTypes.constant.getDisplayName(), "constant value");
		assertEquals(DecisionCompTypes.context, DecisionCompTypes.getByName("context"));
		assertEquals(DecisionCompTypes.constant, DecisionCompTypes.getByName("constant"));
	}
	
	@Test
	public void testFlowContext(){
		
		List<String> list = Arrays.asList("qw", "we", "re");
		Map<String, Object> map = new HashMap<>();
		map.put("var1", 123);
		map.put("list", list);
		
		FlowContext context = new FlowContext(map);
		context.setLocale(Locale.CANADA);
		String str =  context.toString();
		logger.debug(str);
		assertTrue(str.contains("var1"));
		assertTrue(str.contains("123"));
		assertTrue(str.contains("list"));
		assertEquals(Locale.CANADA, context.getLocale());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testFlowContextGetParameters(){
		
		List<String> list = Arrays.asList("qw", "we", "re");
		Map<String, Object> map = new HashMap<>();
		map.put("var1", 123);
		map.put("list", list);
		
		FlowContext context = new FlowContext(map);
		context.getParameters().remove("list");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testFlowContextGetKeys(){
		
		List<String> list = Arrays.asList("qw", "we", "re");
		Map<String, Object> map = new HashMap<>();
		map.put("var1", 123);
		map.put("list", list);
		
		FlowContext context = new FlowContext(map);
		context.keySet().remove("list");

	}

}
