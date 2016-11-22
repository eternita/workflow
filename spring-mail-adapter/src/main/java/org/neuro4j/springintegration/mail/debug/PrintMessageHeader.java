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
package org.neuro4j.springintegration.mail.debug;


import static org.neuro4j.springintegration.mail.debug.PrintMessageHeader.IN_MESSAGE;

import java.util.Map;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
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
public class PrintMessageHeader implements ActionBlock {
    
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
