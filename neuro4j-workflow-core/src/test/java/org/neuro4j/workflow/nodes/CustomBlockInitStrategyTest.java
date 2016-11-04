package org.neuro4j.workflow.nodes;

import static junit.framework.Assert.fail;
import static org.junit.Assert.*;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.core.SystemOutBlock;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;

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
	public void testDefaultStrategyWithNull() throws FlowExecutionException {
		CustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.loadCustomBlock(null);
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testDefaultStrategyLoadClassNotImplementedActionBlock() throws FlowExecutionException {
		CustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.loadCustomBlock("org.neuro4j.workflow.WorkflowRequest");
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testGetCustomBlockClassNotImplemented() throws FlowExecutionException {
		DefaultCustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.getCustomBlockClass("org.neuro4j.workflow.WorkflowRequest");
	}
	
	@Test(expected=FlowExecutionException.class)
	public void testgetCustomBlockClassWithUnknowClass() throws FlowExecutionException {
		DefaultCustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.getCustomBlockClass("org.neuro4j.workflow.core.SomeClass");
	}
	@Test(expected= FlowExecutionException.class)
	public void testgetCustomBlockClassWithNull() throws FlowExecutionException {
		DefaultCustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		strategy.getCustomBlockClass(null);
	}

	@Test
	public void testgetCustomBlockClassWithNotDefaultConstructor() {
		DefaultCustomBlockInitStrategy strategy = new DefaultCustomBlockInitStrategy();
		try {
			strategy.loadCustomBlock("org.neuro4j.workflow.core.CustomBlockNoDefaultConstructor");
			fail("Should be exception");
		} catch (FlowExecutionException e) {
			assertEquals(
					"CustomBlock: org.neuro4j.workflow.core.CustomBlockNoDefaultConstructor can not be initialized.",
					e.getMessage());

		}
	}
	
}
