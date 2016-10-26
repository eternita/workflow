package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.UUID;
import org.junit.Test;
import org.junit.internal.matchers.IsCollectionContaining;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.Transition;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class JoinNodeTest {
	
	@Test
	public void testJoinNode(){
		String name = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		JoinNode  joinNode = new JoinNode(name, uuid);

		String endName = UUID.randomUUID().toString();
		String endUuid = UUID.randomUUID().toString();
		
		EndNode end1 = new EndNode(endName, endUuid);
		
		Transition transition = new Transition();
		transition.setName(SWFConstants.NEXT_RELATION_NAME);
		transition.setToNode(end1);
		
		joinNode.registerExit(transition);
		
		assertThat(joinNode.getExitByName(SWFConstants.NEXT_RELATION_NAME), is(transition));
		assertThat(joinNode.getExits(), IsCollectionContaining.hasItem(transition));
		
		assertEquals(name, joinNode.getName());
		assertEquals(uuid, joinNode.getUuid());
		
		assertEquals(endName, end1.getName());
		assertEquals(endUuid, end1.getUuid());
		
		assertEquals("NodeInfo [uuid=" + uuid + ", name=" + name + "]", joinNode.getNodeInfo().toString());
		try {
			joinNode.validate(null, new FlowContext());
			fail("Should be exception");
		} catch (FlowExecutionException e) {
           assertEquals("JoinBlock: Wrong configuration", e.getMessage());
		}
		
		try {
			joinNode.init();
		} catch (Exception e) {
			fail("Should not be exception");
		}
		
		try {
			joinNode.validate(null, new FlowContext());
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
		
		WorkflowRequest request = new WorkflowRequest();
		
		try {
			joinNode.execute(null, request);
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}
		
		assertEquals(transition, request.getNextWorkflowNode());
	}
	
	
}
