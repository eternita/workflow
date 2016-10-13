package org.neuro4j.workflow.common;

/**
 *
 */
public interface WorkflowSource {
	
	String location();
	
	Workflow content() throws FlowExecutionException;
	
	long lastModified();

}
