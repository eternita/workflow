package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.LoopNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowNode;
import org.neuro4j.workflow.node.WorkflowProcessor;

public class LoopNodeTest {

	@Test
	public void testLoopNode() {

		WorkflowRequest request = new WorkflowRequest();
		request.addParameter("list", Arrays.asList("a", "b", "c"));

		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		LoopNode node = new LoopNodeBuilder().withTransition(SWFConstants.NEXT_RELATION_NAME)
				.withTransition("LOOP_EXIT").build();

		node.setIteratorKey("list");
		node.setElementKey("el1");

		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		try {
			node.execute(processor, request);
			String el1 = (String) request.getLogicContext().get("el1");
			assertEquals("a", el1);
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testLoopNodeWithoutIteratorKey() {

		WorkflowRequest request = new WorkflowRequest();
		request.addParameter("list", Arrays.asList("a", "b", "c"));

		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		LoopNode node = new LoopNodeBuilder().withTransition(SWFConstants.NEXT_RELATION_NAME)
				.withTransition("LOOP_EXIT").build();

		node.setElementKey("el1");

		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		try {
			Transition tr = node.execute(processor, request);
			String el1 = (String) request.getLogicContext().get("el1");
			assertNull(el1);
			assertNotNull(tr);
			assertEquals("NEXT", tr.getName());
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}

	}
	
	@Test
	public void testLoopNodeValidation() {

		WorkflowRequest request = new WorkflowRequest();


		WorkflowProcessor processor = new WorkflowProcessor(new ConfigBuilder());

		LoopNode node = new LoopNodeBuilder().withTransition(SWFConstants.NEXT_RELATION_NAME)
				.withTransition("LOOP_EXIT").build();

		node.setElementKey("el1");

		try {
			node.init();
		} catch (FlowExecutionException e1) {
			fail(e1.getMessage());
		}

		try {
			node.validate(processor, request.getLogicContext());
			fail("should be exception");
		} catch (FlowExecutionException e) {
			e.printStackTrace();
            assertTrue(e.getMessage().startsWith("LoopBlock uuid:"));
		}

	}
	
	public static class LoopNodeBuilder {
		LoopNode node;

		WorkflowNode loopExit;
		WorkflowNode nextExit;

		public LoopNodeBuilder() {

			String name = UUID.randomUUID().toString() + "Name";
			String uuid = UUID.randomUUID().toString() + "uuid";

			node = new LoopNode(name, uuid);
		}

		public LoopNodeBuilder withTransition(String name) {

			String endName = UUID.randomUUID().toString();
			String endUuid = UUID.randomUUID().toString();

			JoinNode end1 = new JoinNode(endName, endUuid);

			Transition transition = new Transition();
			transition.setName(name);
			transition.setToNode(end1);

			node.registerExit(transition);

			return this;

		}

		public LoopNode build() {
			return node;
		}

	}

}
