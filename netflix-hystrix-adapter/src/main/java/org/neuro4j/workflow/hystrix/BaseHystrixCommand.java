package org.neuro4j.workflow.hystrix;

import static org.neuro4j.workflow.enums.ActionBlockCache.NONE;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.DEFAULT_COMMAND_KEY;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.DEFAULT_GROUP_KEY;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.getSetterForGroup;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.enums.CachedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CachedNode(type=NONE)
public abstract class BaseHystrixCommand extends HystrixLifecycleCommand implements ActionBlock {

	private static Logger logger = LoggerFactory.getLogger(BaseHystrixCommand.class);
	
	
	protected BaseHystrixCommand() {
		this(getSetterForGroup(DEFAULT_GROUP_KEY, DEFAULT_COMMAND_KEY));
	}
    protected BaseHystrixCommand(Setter setter) {
        super(setter);
    }
	
	
	@Override
	protected abstract Integer executeInternal(FlowContext ctx) throws Exception;

	@Override
	public final int execute(FlowContext context) throws FlowExecutionException {
		setFlowContext(context);
		return execute();
	}
	
	@Override
    protected Integer getFallback() {
        logger.debug("Running fallback for command {}", this.getClass().getSimpleName());
       return ERROR;
    }

}
