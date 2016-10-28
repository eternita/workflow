package org.neuro4j.workflow.common;

import static org.junit.Assert.*;

import java.util.UUID;

import org.hamcrest.collection.IsArrayContaining;
import org.junit.Test;
import org.neuro4j.workflow.enums.FlowVisibility;
import org.neuro4j.workflow.enums.StartNodeTypes;

public class CommonTest {
	
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

}
