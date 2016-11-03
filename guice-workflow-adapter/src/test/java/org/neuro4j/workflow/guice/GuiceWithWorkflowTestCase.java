/**
 * 
 */
package org.neuro4j.workflow.guice;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.guice.flows.CustomBlockWithService;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

/**
 *
 *
 */
public class GuiceWithWorkflowTestCase {
	
	private static final Logger Logger = LoggerFactory.getLogger(GuiceWithWorkflowTestCase.class);


    GuiceCustomBlockInitStrategy initStrategy = null;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // Create guice module
        AppInjector module = new AppInjector();
        
        List<Module> modules = new ArrayList<Module>();
        
        modules.add(module);
        
        // Register Guice strategy in workflow engine.
        initStrategy = new GuiceCustomBlockInitStrategy(modules);
        
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
        WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader(new XmlWorkflowConverter())).withCustomBlockInitStrategy(initStrategy));

        engine.execute("org.neuro4j.workflow.guice.flows.Flow-Start");
    }
    
    @Test
    public void testCustomBlock() {
        try {
            CustomBlockWithService cb = (CustomBlockWithService) initStrategy.loadCustomBlock(CustomBlockWithService.class.getCanonicalName());
            cb.execute(new FlowContext());
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            fail();
        }
    }
    
    @Test(expected=FlowExecutionException.class)
    public void testWrongCustomBlock() throws FlowExecutionException {
    	initStrategy.loadCustomBlock("someclass1.class");
    }
    
	@Test
	public void testConfigBuilderWithInitStrategy() throws FlowExecutionException{
	    	ConfigBuilder builder = new ConfigBuilder().withCustomBlockInitStrategy(initStrategy);
	        assertNotNull(builder.getCustomInitStrategy());
	        assertThat(builder.getCustomInitStrategy(), instanceOf(GuiceCustomBlockInitStrategy.class));
	        assertNotNull(builder.getLoader());	
	        assertThat(builder.getLoader(), instanceOf(RemoteWorkflowLoader.class));
	        assertNotNull(builder.getWorkflowCache());	
	        assertThat(builder.getWorkflowCache(), instanceOf(EmptyWorkflowCache.class));
	        assertThat(builder.getActionRegistry(), instanceOf(ActionHandlersRegistry.class));
	}

}
