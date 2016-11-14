package org.neuro4j.workflow.async;

import static org.junit.Assert.fail;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.loader.WorkflowLoader;

public class FakeWorkflowLoader implements WorkflowLoader {

	@Override
	public Workflow load(String name) throws FlowExecutionException {
		fail("should not be here");
		throw new FlowExecutionException("It should not be there!");
	}

}