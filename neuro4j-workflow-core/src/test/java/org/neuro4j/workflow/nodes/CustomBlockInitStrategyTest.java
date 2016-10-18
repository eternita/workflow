package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.UUID;
import static org.hamcrest.collection.IsCollectionContaining.*;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.core.SystemOutBlock;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.Transition;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CustomBlockInitStrategyTest {
	
	@Test
	public void testDefaultStrategy() {
		CustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();

		try {
			ActionBlock action = strategy.loadCustomBlock("org.neuro4j.workflow.core.SystemOutBlock");
			assertNotNull(action);
			assertThat(action, IsInstanceOf.instanceOf(SystemOutBlock.class));
		} catch (FlowExecutionException e) {
			fail(e.getMessage());
		}

	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultStrategyWithUnknowClass() throws FlowExecutionException {
		CustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.loadCustomBlock("org.neuro4j.workflow.core.SomeClass");
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultStrategyLoadClassNotImplementedActionBlock() throws FlowExecutionException {
		CustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.loadCustomBlock("org.neuro4j.workflow.WorkflowRequest");
	}
	
}
