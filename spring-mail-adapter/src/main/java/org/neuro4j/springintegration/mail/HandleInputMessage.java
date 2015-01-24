package org.neuro4j.springintegration.mail;


import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.common.TriggerBlock;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import static org.neuro4j.springintegration.mail.HandleInputMessage.*;

/**
 * Receive message and call flow 
 *
 */
@ParameterDefinitionList(input={
                                	},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=true, type= "org.springframework.messaging.Message")})

public class HandleInputMessage extends TriggerBlock implements MessageHandler {
    
    static final String OUT_MESSAGE = "message"; 
    

	public void handleMessage(Message<?> message) throws MessagingException {
        Map<String, Object> parameters = new HashMap<String, Object>();

        // put output parameter to map.
        parameters.put(OUT_MESSAGE, message); 
    
        ExecutionResult result = executeFlow(parameters);
		
	}
	

}
