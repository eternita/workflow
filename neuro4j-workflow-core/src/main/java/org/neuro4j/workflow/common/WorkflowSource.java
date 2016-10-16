package org.neuro4j.workflow.common;

/**
 * Base interface to retrieve workflow from stream.
 */
public interface WorkflowSource {
	
	/**
	 * Returns name of workflow ex. org.neuro4j.common.LoadProfile
	 * @return the name
	 */
	String name();
	
	/**
	 * Returns workflow object retrieved from loader (Remote, Classpath, XMLFile, JSONFile)
	 * @return Workflow
	 * @throws FlowExecutionException in case of error
	 */
	Workflow content() throws FlowExecutionException;
	
	/**
	 * Returns lastModified
	 * @return lastModified time
	 */
	long lastModified();

}
