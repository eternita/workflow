package org.neuro4j.springframework.jms.example;


import javax.jms.Message;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.CustomBlock;

import static org.neuro4j.springframework.jms.example.DoSomething.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_MESSAGE, isOptional=true, type= "javax.jms.Message")},
                         output={
                         	        })	
public class DoSomething extends CustomBlock {
    
    static final String IN_MESSAGE = "message";
      
    
    
    
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
    	Message message = (Message) ctx.get(IN_MESSAGE);
        
		
    	Logger.info(this, "Working with message {}", message);
		
		
		return NEXT;
	}
	
	
	public void init() throws FlowInitializationException{
		super.init();
	}
	

}
