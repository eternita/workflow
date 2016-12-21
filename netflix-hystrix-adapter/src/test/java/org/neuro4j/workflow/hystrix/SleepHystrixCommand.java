package org.neuro4j.workflow.hystrix;


import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.DEFAULT_COMMAND_KEY;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.getSetterForGroup;
import static org.neuro4j.workflow.hystrix.SleepHystrixCommand.IN_PARAM1;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_PARAM1, isOptional=false, type= "java.lang.Long")})	
                        	       
public class SleepHystrixCommand extends BaseHystrixCommand {
    
    static final String IN_PARAM1 = "param1";

	public SleepHystrixCommand() {
		super(getSetterForGroup("TestGroupKey", DEFAULT_COMMAND_KEY));
	}
    
	@Override
	protected Integer executeInternal(FlowContext ctx) throws Exception {
		
		Long sleepMs = (Long)ctx.get(IN_PARAM1);
		
		
		Thread.currentThread().sleep(sleepMs);
		

		return NEXT;
	}
	
	

}
