package org.neuro4j.workflow.common;

/**
 * Base interface to load workflow.
 */
public interface WorkflowLoader {
	
	/**
	 * Loads workflow by name
	 * @param name of workflow
	 * @return WorkflowSource
	 * @throws FlowExecutionException in case of workflow can not be loaded
	 */
	public WorkflowSource load(final String name) throws FlowExecutionException;

}

