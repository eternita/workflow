package org.neuro4j.workflow.fork;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.CallNode;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.LoopNode;
import org.neuro4j.workflow.node.SwitchNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowNode;
import org.neuro4j.workflow.node.WorkflowProcessor;
import org.neuro4j.workflow.nodes.LoopNodeTest.LoopNodeBuilder;

public class ForkTest {
	
	WorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	}
	
	@Test
	public void testCallWorkflowWithFork() {
		
		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(converter)));
		Map<String, Object> parameters = new HashMap<String, Object>();
		String randomStr = UUID.randomUUID().toString();
		parameters.put("message", randomStr);

		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.ForkWorkflow-StartNode1", parameters);
		
		
		


	}
	

	
	
	public static class SwitchNodeBuilder {
		SwitchNode node;

		WorkflowNode loopExit;
		WorkflowNode nextExit;

		public SwitchNodeBuilder() {

			String name = UUID.randomUUID().toString() + "Name";
			String uuid = UUID.randomUUID().toString() + "uuid";

			node = new SwitchNode(name, uuid, "default");
		}

		public SwitchNodeBuilder withJoinTransition(String name) {

			String endName = UUID.randomUUID().toString();
			String endUuid = UUID.randomUUID().toString();

			JoinNode end1 = new JoinNode(endName, endUuid);

			Transition transition = new Transition();
			transition.setName(name);
			transition.setToNode(end1);

			node.registerExit(transition);

			return this;

		}
		
		public SwitchNodeBuilder withTransitionTo(String name, WorkflowNode fromNode, WorkflowNode toNode) {

			Transition transition = new Transition();
			transition.setName(name);
			transition.setToNode(toNode);

			fromNode.registerExit(transition);

			return this;

		}

		public SwitchNode build() {
			return node;
		}

	}
	
}
