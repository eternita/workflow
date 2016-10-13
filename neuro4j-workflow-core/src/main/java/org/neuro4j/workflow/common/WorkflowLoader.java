package org.neuro4j.workflow.common;

/**
 *
 */
public interface WorkflowLoader {
	
	public WorkflowSource load(final String location) throws FlowExecutionException;

}

