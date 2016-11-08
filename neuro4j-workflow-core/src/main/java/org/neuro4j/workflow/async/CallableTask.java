package org.neuro4j.workflow.async;

import java.util.concurrent.Callable;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.node.WorkflowProcessor;

public class CallableTask implements Callable<ExecutionResult> {

	private final WorkflowProcessor processor;
	private final String flow;
	private final WorkflowRequest request;

	public CallableTask(final WorkflowProcessor processor, final String flow, final WorkflowRequest request) {
		this.processor = processor;
		this.flow = flow;
		this.request = request;
	}

	@Override
	public ExecutionResult call() throws Exception {
		return processor.execute(flow, request);
	}

}
