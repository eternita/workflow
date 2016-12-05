/**
 * 
 */
package org.neuro4j.workflow.spring;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neuro4j.springframework.context.SpringContextInitStrategy;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;
import org.neuro4j.workflow.spring.service.MyMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextWorkflowTest {

	private static final Logger Logger = LoggerFactory.getLogger(SpringContextWorkflowTest.class);

	@Autowired
	SpringContextInitStrategy initStrategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFlow() throws Exception {

		// Now all custom blocks will be initialized with Guice.
		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withCustomBlockInitStrategy(initStrategy));

		ExecutionResult result = engine.execute("org.neuro4j.workflow.spring.Flow-Start");
		assertEquals("Message to Mister = Hi", result.getFlowContext().get("out1"));

	}

	@Test
	public void testCustomBlock() {
		try {
			CustomBlockWithService cb = (CustomBlockWithService) initStrategy
					.loadCustomBlock(CustomBlockWithService.class.getCanonicalName());
			cb.execute(new FlowContext());
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			fail();
		}
	}

	@Test(expected = FlowExecutionException.class)
	public void testWrongCustomBlock() throws FlowExecutionException {
		initStrategy.loadCustomBlock("someclass1.class");
	}

	public void testLoadCustomBlock() throws FlowExecutionException {
		CustomBlockWithService block = (CustomBlockWithService) initStrategy
				.loadCustomBlock("org.neuro4j.workflow.spring.CustomBlockWithService");
		assertThat(block.service, instanceOf(MyMessageService.class));
	}

	@Test
	public void testConfigBuilderWithInitStrategy() throws FlowExecutionException {
		ConfigBuilder builder = new ConfigBuilder().withCustomBlockInitStrategy(initStrategy);
		assertNotNull(builder.getCustomInitStrategy());
		assertThat(builder.getCustomInitStrategy(), instanceOf(SpringContextInitStrategy.class));
		assertNotNull(builder.getLoader());
		assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
		assertNotNull(builder.getWorkflowCache());
		assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
		assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}
	
	@Test
	public void testLoadNoComponentCustomBlock() throws FlowExecutionException {
		ActionBlock action = initStrategy.loadCustomBlock("org.neuro4j.workflow.spring.CustomBlockNoComponent");
		FlowContext context = new FlowContext();
		assertEquals(1, action.execute(context));
		assertEquals("somevalue", context.get("somekey"));
	}

}
