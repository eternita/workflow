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

import static org.neuro4j.springframework.jms.GetTextMessageCreator.IN_MESSAGE;
import static org.neuro4j.springframework.jms.GetTextMessageCreator.OUT_MESSAGECREATOR;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.jms.core.MessageCreator;

/**
 * Creates text message creator.
 * 
 */
@ParameterDefinitionList(input = {
		@ParameterDefinition(name = IN_MESSAGE, isOptional = true, type = "java.lang.String") }, output = {
				@ParameterDefinition(name = OUT_MESSAGECREATOR, isOptional = true, type = "org.springframework.jms.core.MessageCreator") })
public class GetTextMessageCreator implements ActionBlock {

	static final String IN_MESSAGE = "message";

	static final String OUT_MESSAGECREATOR = "messageCreator";

	public int execute(FlowContext ctx) throws FlowExecutionException {

		final String message = (String) ctx.get(IN_MESSAGE);

		if (message == null) {
			return ERROR;
		}

		MessageCreator messageCreator = new MessageCreator() {

			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}

		};

		ctx.put(OUT_MESSAGECREATOR, messageCreator);

		return NEXT;
	}

}
