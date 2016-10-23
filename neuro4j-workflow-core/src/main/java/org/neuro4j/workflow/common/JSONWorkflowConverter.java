package org.neuro4j.workflow.common;

import java.io.Reader;

import org.neuro4j.workflow.utils.Validation;

public class JSONWorkflowConverter implements WorkflowConverter{

	@Override
	public Workflow convert(Reader stream, String name) throws FlowExecutionException {
		Validation.requireNonNull(stream, () -> new FlowExecutionException("InputStream can not be null"));
          throw new UnsupportedOperationException("Not supported yet");
	}

	@Override
	public String getFileExt() {
		 throw new UnsupportedOperationException("Not supported yet");
	}

}
