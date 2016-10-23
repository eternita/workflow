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
package org.neuro4j.springintegration.mail;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * This class creates trigger node and subscribes to inputChannel 
 *
 */
public class MessageHandlerFactoryBean {

	@Autowired
	WorkflowEngine engine;

	/**
	 * The constructor.
	 */
	public MessageHandlerFactoryBean() {
	}

	/**
	 * Creates and subscribes flow
	 * 
	 * @param inputChannel
	 * @param flow trigger path (eg. org.neuro4j.MyFlow-MyTriggerNode)
	 * @return MessageHandler object.
	 * @throws FlowInitializationException
	 */
	public MessageHandler initMessageHandler(DirectChannel inputChannel, final String flow) throws FlowInitializationException{
		
		if (inputChannel == null){
			throw new FlowInitializationException("inputChannel is NULL");
		}

		MessageHandler messageHandler = new MessageHandler() {
			
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
		        Map<String, Object> parameters = new HashMap<String, Object>();

		        // put output parameter to map.
		        parameters.put("message", message); 
		        engine.execute(flow , parameters);
			}
		};
		
		return messageHandler;
		
	}

}
