package org.neuro4j.workflow.common;

import java.io.Reader;

public interface WorkflowConverter {
	
	public final static String DEFAULT_EXT = ".n4j";
	
	Workflow convert(Reader stream, String name) throws FlowExecutionException;
	
	String getFileExt();

}
