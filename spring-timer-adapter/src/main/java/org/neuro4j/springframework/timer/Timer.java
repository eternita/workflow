package org.neuro4j.springframework.timer;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.node.CustomBlock;


public class Timer extends CustomBlock
{

	public void execute() {		
	}

	@Override
	public int execute(FlowContext context) throws FlowExecutionException {
		return 0;
	}
}
