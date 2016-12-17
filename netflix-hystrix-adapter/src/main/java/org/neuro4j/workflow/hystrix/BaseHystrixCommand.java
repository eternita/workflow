package org.neuro4j.workflow.hystrix;

import static org.neuro4j.workflow.enums.ActionBlockCache.NONE;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.enums.CachedNode;

@CachedNode(type=NONE)
public class BaseHystrixCommand extends AbstractHystrixCommand implements ActionBlock {

	@Override
	protected Integer executeInternal(FlowContext ctx) throws Exception {
		return NEXT;
	}

	@Override
	public final int execute(FlowContext context) throws FlowExecutionException {
		setFlowContext(context);
		return execute();
	}

}
