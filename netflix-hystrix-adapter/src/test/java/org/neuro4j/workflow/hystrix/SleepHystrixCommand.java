package org.neuro4j.workflow.hystrix;


import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.DEFAULT_COMMAND_KEY;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.getSetterForGroup;
import static org.neuro4j.workflow.hystrix.SleepHystrixCommand.IN_PARAM1;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_PARAM1, isOptional=false, type= "java.lang.Long")})	
                        	       
public class SleepHystrixCommand extends BaseHystrixCommand {
    
    static final String IN_PARAM1 = "param1";
	private static final Logger Logger = LoggerFactory.getLogger(SleepHystrixCommand.class);
	
	public SleepHystrixCommand() {
		super(getSetterForGroup("TestGroupKey1", "TestCommandKey1"));
	}
    
	@Override
	protected Integer executeInternal(FlowContext ctx) throws Exception {

		Long sleepMs = (Long) ctx.get(IN_PARAM1);

		try {
			Thread.currentThread().sleep(sleepMs);
		} catch (InterruptedException ex) {
			Logger.error(ex.getMessage(), ex);
		}

		return NEXT;
	}
	
	

}
