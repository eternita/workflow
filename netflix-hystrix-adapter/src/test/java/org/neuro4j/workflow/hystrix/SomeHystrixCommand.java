package org.neuro4j.workflow.hystrix;


import static org.neuro4j.workflow.hystrix.SomeHystrixCommand.IN_PARAM1;
import static org.neuro4j.workflow.hystrix.SomeHystrixCommand.OUT_PARAM;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.hystrix.BaseHystrixCommand;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_PARAM1, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_PARAM, isOptional=true, type= "java.lang.String")})	
                        	       
public class SomeHystrixCommand extends BaseHystrixCommand {
    
    static final String IN_PARAM1 = "param1";
      
    static final String OUT_PARAM = "param"; 
    
    
    
	@Override
	protected Integer executeInternal(FlowContext ctx) throws Exception {
		
		String name = (String)ctx.get(IN_PARAM1);
		
		String message  = "Hello, " + name;
		
		ctx.put(OUT_PARAM, message);
		return NEXT;
	}
	
	

}
