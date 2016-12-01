/**
 * 
 */
package org.neuro4j.workflow.governator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.governator.flows.CustomBlockWithService;
import org.neuro4j.workflow.governator.service.MyMessageService;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.netflix.governator.guice.LifecycleInjector;

/**
 *
 *
 */
public class GovernatorWithWorkflowTest {

	private static final Logger Logger = LoggerFactory.getLogger(GovernatorWithWorkflowTest.class);

	GovernatorCustomBlockInitStrategy initStrategy = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		Injector injector = LifecycleInjector.builder().withModuleClass(AppInjector.class)
				.usingBasePackages("org.neuro4j").build().createInjector();

		initStrategy = new GovernatorCustomBlockInitStrategy(injector);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFlow() {
		// Now all custom blocks will be initialized with Guice.
		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(new XmlWorkflowConverter()))
						.withCustomBlockInitStrategy(initStrategy));

		ExecutionResult result = engine.execute("org.neuro4j.workflow.governator.flows.Flow-Start");
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
				.loadCustomBlock("org.neuro4j.workflow.governator.flows.CustomBlockWithService");
		assertThat(block.getService(), instanceOf(MyMessageService.class));
	}

	@Test
	public void testConfigBuilderWithInitStrategy() throws FlowExecutionException {
		ConfigBuilder builder = new ConfigBuilder().withCustomBlockInitStrategy(initStrategy);
		assertNotNull(builder.getCustomInitStrategy());
		assertThat(builder.getCustomInitStrategy(), instanceOf(GovernatorCustomBlockInitStrategy.class));
		assertNotNull(builder.getLoader());
		assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
		assertNotNull(builder.getWorkflowCache());
		assertThat(builder.getWorkflowCache(), instanceOf(ConcurrentMapWorkflowCache.class));
		assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}

}
