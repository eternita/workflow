/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.neuro4j.springframework.jms;


import static org.neuro4j.springframework.jms.JMSQueueSender.IN_MESSAGE_CREATOR;
import static org.neuro4j.springframework.jms.JMSQueueSender.IN_MESSAGE_DESTINATION;

import javax.jms.ConnectionFactory;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * Send a message to the specified destination.
 *
 */
@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_MESSAGE_CREATOR, isOptional=true, type= "org.springframework.jms.core.MessageCreator"),
                                	@ParameterDefinition(name=IN_MESSAGE_DESTINATION, isOptional=true, type= "java.lang.String")},
                         output={
                         	        })	
@Component
public class JMSQueueSender extends CustomBlock {
   
	
    private JmsTemplate jmsTemplate;

	
    static final String IN_MESSAGE_CREATOR = "messageCreator";
    static final String IN_MESSAGE_DESTINATION = "messageDestination";
      
    
    @Autowired
    public void setConnectionFactory(ConnectionFactory cf) {
        this.jmsTemplate = new JmsTemplate(cf);
    }

    
    
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
    	MessageCreator messageCreator = (MessageCreator) ctx.get(IN_MESSAGE_CREATOR);
    	String messageDestination = (String) ctx.get(IN_MESSAGE_DESTINATION);
        
		
        if (messageCreator == null || messageDestination == null)
        {
        	return ERROR;
        }
		
        this.jmsTemplate.send(messageDestination, messageCreator);
		
		return NEXT;
	}
	
	

}
