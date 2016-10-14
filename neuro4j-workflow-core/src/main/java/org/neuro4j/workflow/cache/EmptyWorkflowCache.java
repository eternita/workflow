package org.neuro4j.workflow.cache;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowLoader;
import org.neuro4j.workflow.common.WorkflowSource;

public enum EmptyWorkflowCache implements WorkflowCache {
	INSTANCE;


	@Override
	public Workflow get(WorkflowLoader loader,  String flowName) throws  FlowExecutionException {
		WorkflowSource workflowSource = loader.load(flowName);
		if (workflowSource == null) {
			throw new FlowExecutionException("Workflow " + flowName + " not loaded");
		}

		Workflow workflow = workflowSource.content();
		return workflow;
	}

	@Override
	public void clearAll() {
	}

	@Override
	public void clear(String key) {
	}


}
