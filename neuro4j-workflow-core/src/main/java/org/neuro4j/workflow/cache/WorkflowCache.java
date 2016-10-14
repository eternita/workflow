package org.neuro4j.workflow.cache;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowLoader;

public interface WorkflowCache {

	public void clearAll();

	public void clear(String key);

	public Workflow get(WorkflowLoader loader, String flow) throws FlowExecutionException;
}
