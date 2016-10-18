package org.springframework.samples.mvc.simple;


import static org.springframework.samples.mvc.simple.SayHello.IN_VAR1;
import static org.springframework.samples.mvc.simple.SayHello.OUT_VAR2;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_VAR1, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_VAR2, isOptional=true, type= "java.lang.String")})	
public class SayHello extends CustomBlock {
    
    static final String IN_VAR1 = "var1";
      
    static final String OUT_VAR2 = "var2"; 
    
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		
		ctx.put(OUT_VAR2, "Hello World!"); 
    
		
		return NEXT;
	}
	
	

}
