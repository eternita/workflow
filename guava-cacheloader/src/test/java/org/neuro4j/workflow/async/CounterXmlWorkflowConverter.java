package org.neuro4j.workflow.async;

import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.XmlWorkflowConverter;

public class CounterXmlWorkflowConverter implements WorkflowConverter{
	
	XmlWorkflowConverter converter = new XmlWorkflowConverter();
	
	AtomicInteger counter = new AtomicInteger(0);


	@Override
	public Workflow convert(Reader reader, String name) throws FlowExecutionException {
		counter.incrementAndGet();
		return converter.convert(reader, name);
	}

	@Override
	public String getFileExt() {
		return converter.getFileExt();
	}

	public AtomicInteger getCounter() {
		return counter;
	}
	
	
}