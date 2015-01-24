package org.neuro4j.springintegration.mail.debug;


import static org.neuro4j.springintegration.mail.debug.PrintMessageHeader.IN_MESSAGE;

import java.util.Map;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * Prints message headers to System.out
 *
 */
@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_MESSAGE, isOptional=true, type= "org.springframework.messaging.Message")},
                         output={
                         	        })	
public class PrintMessageHeader extends CustomBlock {
    
    static final String IN_MESSAGE = "message";
      
    
    
    
    /* (non-Javadoc)
     * @see org.neuro4j.workflow.ActionBlock#execute(org.neuro4j.workflow.FlowContext)
     */
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
    	Message<?> message = (Message<?>)ctx.get(IN_MESSAGE);
        
        if (message == null)
        {
        	return ERROR;
        }
		
        MessageHeaders headers = message.getHeaders();
        
        for(Map.Entry<String, Object> header: headers.entrySet())
        {
        	System.out.println("Key: " + header.getKey() + " Value: " + header.getValue());
        }
        
		
		return NEXT;
	}
	

}
