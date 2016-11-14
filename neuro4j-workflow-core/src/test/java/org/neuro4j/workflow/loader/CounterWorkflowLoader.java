package org.neuro4j.workflow.loader;

import java.util.concurrent.atomic.AtomicInteger;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;

public class CounterWorkflowLoader implements WorkflowLoader{

	AtomicInteger counter = new AtomicInteger(0);
	
	private final WorkflowLoader delegate;
	
	public CounterWorkflowLoader(WorkflowLoader delegate){
		this.delegate = delegate;
	}
	
	@Override
	public Workflow load(String name) throws FlowExecutionException {
		counter.incrementAndGet();
		return delegate.load(name);
	}
	
	public AtomicInteger getCounter(){
		return counter;
	}

}
