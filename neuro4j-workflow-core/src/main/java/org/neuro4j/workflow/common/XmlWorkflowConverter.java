package org.neuro4j.workflow.common;

import java.io.Reader;

import org.neuro4j.workflow.loader.f4j.FlowConverter;
import org.neuro4j.workflow.utils.Validation;

public class XmlWorkflowConverter implements WorkflowConverter{
	
	final String fileExt;
	
	public XmlWorkflowConverter(final String ext){
		this.fileExt = ext;
	}
	
	public XmlWorkflowConverter(){
		this(DEFAULT_EXT);
	}

	@Override
	public Workflow convert(Reader stream, String name) throws FlowExecutionException {
		Validation.requireNonNull(stream, () -> new FlowExecutionException("InputStream can not be null"));
		try {
			return FlowConverter.xml2workflow(stream, name);
		} catch (Exception e) {
			throw new FlowExecutionException(e);
		}
	}

	@Override
	public String getFileExt() {
		return this.fileExt;
	}
	
	

}
