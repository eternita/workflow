package org.neuro4j.example.helloword;


import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

import static org.neuro4j.example.helloword.SayHello.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_NAME, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=true, type= "java.lang.String")})	
public class SayHello extends CustomBlock {
    
    static final String IN_NAME = "name";
      
    static final String OUT_MESSAGE = "message"; 
    
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		Object name = ctx.get(IN_NAME);
        
		
		String message = "Hello " + name;
		

		
		ctx.put(OUT_MESSAGE, message); 
    
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
