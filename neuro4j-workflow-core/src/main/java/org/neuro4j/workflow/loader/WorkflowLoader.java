package org.neuro4j.workflow.loader;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;

/**
 * Base interface to load workflow.
 */
public interface WorkflowLoader {
	
	/**
	 * Loads workflow by name
	 * @param name of workflow
	 * @return Workflow
	 * @throws FlowExecutionException in case of workflow can not be loaded
	 */
	public Workflow load(final String name) throws FlowExecutionException;

}

