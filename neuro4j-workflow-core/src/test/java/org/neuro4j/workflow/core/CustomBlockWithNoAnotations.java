package org.neuro4j.workflow.core;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;

public class CustomBlockWithNoAnotations implements ActionBlock {

	public int execute(FlowContext ctx) throws FlowExecutionException {

		return NEXT;
	}

}
