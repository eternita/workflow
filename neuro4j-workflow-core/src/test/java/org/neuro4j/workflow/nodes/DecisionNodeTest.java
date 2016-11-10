package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.enums.DecisionCompTypes;
import org.neuro4j.workflow.enums.DecisionOperators;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.DecisionNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowProcessor;

public class DecisionNodeTest {
	

	@Test
	public void testDecisionStringNotEql(){
		testDecisionNode("1234", "123", DecisionOperators.NEQ_STR, DecisionCompTypes.context, SWFConstants.NEXT_RELATION_NAME);
	}
	@Test
	public void testDecisionStringNotEqlFalse(){
		testDecisionNode("1234", "1234", DecisionOperators.NEQ_STR, DecisionCompTypes.context, "FALSE");
	}
	
	@Test
	public void testDecisionObjectEqlTrue(){
		testDecisionNode(new Integer(5), new Integer(5), DecisionOperators.EQ, DecisionCompTypes.context, SWFConstants.NEXT_RELATION_NAME);
	}
	
	@Test
	public void testDecisionObjectNotEqlFalseDiffClasses(){
		testDecisionNode(new Integer(5), new Double(5), DecisionOperators.NEQ, DecisionCompTypes.context, "FALSE");
	}
	@Test
	public void testDecisionObjectEqlFalse(){
		testDecisionNode(new Integer(5), new Integer(6), DecisionOperators.EQ, DecisionCompTypes.context, "FALSE");
	}
	@Test
	public void testDecisionObjectNotEqlTrue(){
		testDecisionNode(new Integer(5), new Integer(7), DecisionOperators.NEQ, DecisionCompTypes.context, SWFConstants.NEXT_RELATION_NAME);
	}
	@Test
	public void testDecisionObjectNotEqlFalse(){
		testDecisionNode(new Integer(7), new Integer(7), DecisionOperators.NEQ, DecisionCompTypes.context, "FALSE");
	}
	
	@Test
	public void testDecisionObjectEqlFalseDiffClasses(){
		// diff classes
		testDecisionNode(new Integer(7), new String("7"), DecisionOperators.EQ, DecisionCompTypes.context, "NEXT");
	}
	
	@Test
	public void testDecisionObjectNotEqlTrueDiffClasses(){
		// diff classes
		testDecisionNode(new Integer(7), new String("7"), DecisionOperators.NEQ, DecisionCompTypes.context, "FALSE");
	}
	@Test
	public void testDecisionNumberLessTrue(){
		// diff classes
		testDecisionNode(new Integer(6), new Integer("7"), DecisionOperators.LESS, DecisionCompTypes.context, "NEXT");
	}
	
	@Test
	public void testDecisionNumberLessDiffClasses(){
		// diff classes
		testDecisionNode(new Integer(6), new Double(7), DecisionOperators.LESS, DecisionCompTypes.context, "NEXT");
	}
	
	@Test
	public void testDecisionNumberLessFalse(){
		testDecisionNode(new Double(7.5), new Integer(6), DecisionOperators.LESS, DecisionCompTypes.context, "FALSE");
	}
	
	@Test
	public void testDecisionNumberLessConstant(){
		// diff classes
		testDecisionNode(new Integer(6), "7", DecisionOperators.LESS, DecisionCompTypes.constant, "NEXT");
	}
	
	@Test
	public void testDecisionNumberLessNull(){
		// diff classes
		testDecisionNode(null, "7", DecisionOperators.LESS, DecisionCompTypes.context, "FALSE");
	}
	
	@Test
	public void testHasElementIterator(){
		List<Integer> list  = Arrays.asList(1,2,3,4,5,6);
		testDecisionNode(list.iterator(), null, DecisionOperators.HAS_EL, DecisionCompTypes.context, "NEXT");
	}
	@Test
	public void testHasElementIteratorEmpty(){
		List<Integer> list  = Arrays.asList();
		testDecisionNode(list.iterator(), null, DecisionOperators.HAS_EL, DecisionCompTypes.context, "FALSE");
	}
	
	@Test
	public void testHasElementEnumeration(){
		List<Integer> list  = Arrays.asList(1,2,3,4,5,6);
		Enumeration<Integer> x = new Vector(list).elements();
		testDecisionNode(x, null, DecisionOperators.HAS_EL, DecisionCompTypes.context, "NEXT");
	}
	@Test
	public void testHasElementEnumerationEmpty(){
		List<Integer> list  = Arrays.asList();
		Enumeration<Integer> x = new Vector(list).elements();
		testDecisionNode(x, null, DecisionOperators.HAS_EL, DecisionCompTypes.context, "FALSE");
	}
	@Test
	public void testHasElementEnumerationNull(){

		testDecisionNode(null, null, DecisionOperators.HAS_EL, DecisionCompTypes.context, "FALSE");
	}
	
	// more
	@Test
	public void testDecisionNumberGREATERTrue(){
		// diff classes
		testDecisionNode(new Integer("7"), new Integer(6), DecisionOperators.GREATER, DecisionCompTypes.context, "NEXT");
	}
	
	@Test
	public void testDecisionNumberGREATERDiffClasses(){
		// diff classes
		testDecisionNode( new Double(7), new Integer(6), DecisionOperators.GREATER, DecisionCompTypes.context, "NEXT");
	}
	
	@Test
	public void testDecisionNumberGREATERFalse(){
		testDecisionNode(new Integer(6), new Double(7.5), DecisionOperators.GREATER, DecisionCompTypes.context, "FALSE");
	}
	
	@Test
	public void testDecisionNumberGREATERConstant(){
		// diff classes
		testDecisionNode("7", new Integer(6), DecisionOperators.GREATER, DecisionCompTypes.constant, "NEXT");
	}
	
	@Test
	public void testDecisionOperators(){

		assertTrue(DecisionOperators.DEFINED.isSingleOperand());
		assertTrue(DecisionOperators.UNDEFINED.isSingleOperand());
		assertTrue(DecisionOperators.EMPTY_STR.isSingleOperand());
		assertTrue(DecisionOperators.HAS_EL.isSingleOperand());
		
		assertFalse(DecisionOperators.NEQ_STR.isSingleOperand());
		assertFalse(DecisionOperators.EQ_STR.isSingleOperand());
		assertFalse(DecisionOperators.EQ.isSingleOperand());
		assertFalse(DecisionOperators.NEQ.isSingleOperand());
		
		assertThat(DecisionOperators.operators(), IsArrayWithSize.arrayWithSize(10));
		
		assertEquals(DecisionOperators.DEFINED, DecisionOperators.getByName("DEFINED"));
		assertEquals(DecisionOperators.UNDEFINED, DecisionOperators.getByName("UNDEFINED"));
		assertEquals(DecisionOperators.EMPTY_STR, DecisionOperators.getByName("EMPTY_STR"));
		assertEquals(DecisionOperators.HAS_EL, DecisionOperators.getByName("HAS_EL"));
		
		assertEquals(DecisionOperators.NEQ_STR, DecisionOperators.getByName("NEQ_STR"));
		assertEquals(DecisionOperators.EQ_STR, DecisionOperators.getByName("EQ_STR"));
		assertEquals(DecisionOperators.EQ, DecisionOperators.getByName("EQ"));
		assertEquals(DecisionOperators.NEQ, DecisionOperators.getByName("NEQ"));
	}
	@Test
	public void testDecisionKeyValidation() {

		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		DecisionNode node = new DecisionNode(name, uuid);
		node.setOperator(DecisionOperators.HAS_EL);
		node.setComparisonKey("var2");

		node.setCompTypes(DecisionCompTypes.context);
		String endName = UUID.randomUUID().toString();
		String endUuid = UUID.randomUUID().toString();
		
		EndNode end1 = new EndNode(endName, endUuid);
		
		Transition transition = new Transition();
		transition.setName(SWFConstants.NEXT_RELATION_NAME);
		transition.setToNode(end1);
		
		node.registerExit(transition);
		assertTrue(transition.isValid());
		
		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		try {
			node.validate(processor, new FlowContext());
			fail("should be exception");
		} catch (FlowExecutionException e) {
			assertEquals("Decision node: decisionKey is not defined", e.getMessage());
		}
	}

	@Test
	public void testOperatorValidation() {

		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		DecisionNode node = new DecisionNode(name, uuid);

		node.setComparisonKey("var2");
		node.setCompTypes(DecisionCompTypes.context);
		
		
		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		try {
			node.validate(processor, new FlowContext());
			fail("should be exception");
		} catch (FlowExecutionException e) {
			assertEquals("Decision node: Operator not defined", e.getMessage());
		}
	}
	@Test
	public void testExitsValidation() {

		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";
		DecisionNode node = new DecisionNode(name, uuid);

		node.setComparisonKey("var2");
		node.setOperator(DecisionOperators.HAS_EL);
		node.setCompTypes(DecisionCompTypes.context);
		
		
		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		try {
			node.validate(processor, new FlowContext());
			fail("should be exception");
		} catch (FlowExecutionException e) {
			assertEquals("Decision node: Connector not defined.", e.getMessage());
		}
	}
	public void testDecisionNode(Object var1, Object var2, DecisionOperators operator, DecisionCompTypes compType, String result){
		
		WorkflowRequest request = new WorkflowRequest();
		request.addParameter("var1", var1);
		request.addParameter("var2", var2);
		
        WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());
		
		String name = UUID.randomUUID().toString() + "Name";
		String uuid = UUID.randomUUID().toString() + "uuid";

		
		DecisionNode  node = new DecisionNode(name, uuid);
		node.setOperator(operator);
		if (DecisionCompTypes.context.equals(compType)){
			node.setComparisonKey("var2");			
		} else {
			node.setComparisonKey(var2.toString());			
		}

		node.setDecisionKey("var1");
		node.setCompTypes(compType);
		
		
		String endName = UUID.randomUUID().toString();
		String endUuid = UUID.randomUUID().toString();
		
		EndNode end1 = new EndNode(endName, endUuid);
		
		Transition transition = new Transition();
		transition.setName(SWFConstants.NEXT_RELATION_NAME);
		transition.setToNode(end1);
		
		node.registerExit(transition);
		
		
		String endName2 = UUID.randomUUID().toString();
		String endUuid2 = UUID.randomUUID().toString();
		
		EndNode end2 = new EndNode(endName2, endUuid2);
		
		Transition transition1 = new Transition();
		transition1.setName("FALSE");
		transition1.setToNode(end2);
		
		node.registerExit(transition1);
		
		assertThat(node.getExitByName(SWFConstants.NEXT_RELATION_NAME), is(transition));
		assertThat(node.getExits(), IsCollectionContaining.hasItem(transition));
		assertThat(node.getExits(), IsCollectionContaining.hasItem(transition1));
		assertEquals(node.getOperator(), operator);
		assertEquals(node.getCompTypes(), compType);
		assertEquals(node.getDecisionKey(), "var1");
		if (DecisionCompTypes.context.equals(compType)){
			assertEquals(node.getComparisonKey(), "var2");			
		} else {
			assertEquals(node.getComparisonKey(), var2.toString());
		}

		assertEquals(name, node.getName());
		assertEquals(uuid, node.getUuid());
		
		assertEquals(endName, end1.getName());
		assertEquals(endUuid, end1.getUuid());
		
		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		
		try {
			node.execute(processor, request);

		} catch (FlowExecutionException e) {
             fail(e.getMessage());
		}
		
		assertEquals(result, request.getNextWorkflowNode().getName());
		
	}
	
	
}
