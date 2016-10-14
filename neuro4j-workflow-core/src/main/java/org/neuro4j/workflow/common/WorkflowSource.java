package org.neuro4j.workflow.common;

/**
 *
 */
public interface WorkflowSource {
	
	String name();
	
	Workflow content() throws FlowExecutionException;
	
	long lastModified();

}
