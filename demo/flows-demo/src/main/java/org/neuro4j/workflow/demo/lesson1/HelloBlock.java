package org.neuro4j.workflow.demo.lesson1;


import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

import static org.neuro4j.workflow.demo.lesson1.HelloBlock.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_NAME, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=false, type= "java.lang.String")})	
public class HelloBlock extends CustomBlock {
    
    static final String IN_NAME = "name";
      
    static final String OUT_MESSAGE = "message"; 
    
    

    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		String name = (String)ctx.get(IN_NAME);
        
		String message = "Hello ";
		
        if (name != null)
        {
        	message += name;
        }
		
		ctx.put(OUT_MESSAGE, message); 
    
		
		return NEXT;
	}
	
	

}
