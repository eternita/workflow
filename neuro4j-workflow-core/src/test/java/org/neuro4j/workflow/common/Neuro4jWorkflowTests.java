package org.neuro4j.workflow.common;

import org.junit.Test;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.Neuro4jEngine.ConfigBuilder;

import static junit.framework.Assert.*;


public class Neuro4jWorkflowTests {
	
	@Test
	public void testCreateEngine() throws FlowExecutionException{
            Neuro4jEngine engine = new Neuro4jEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader()));
            ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.FlowForClasspathLoader-Start1");
            assertNotNull(result);
            assertNull(result.getException());
            assertEquals("End1", result.getLastSuccessfulNodeName());
            
	}


}
