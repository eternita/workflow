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

import static org.neuro4j.springintegration.mail.debug.PrintMessage.IN_MESSAGE;

import java.io.InputStream;

import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.neuro4j.springframework.context.SpringContextInitStrategy;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

/**
 * Prints message to System.out.
 * code from (http://www.tutorialspoint.com/)
 */
@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_MESSAGE, isOptional = true, type = "org.springframework.messaging.Message") }, output = {})
public class PrintMessage extends CustomBlock {

	private static final Logger Logger = LoggerFactory.getLogger(SpringContextInitStrategy.class);

	
	static final String IN_MESSAGE = "message";

	public int execute(FlowContext ctx) throws FlowExecutionException {

		Message<?> message = (Message<?>) ctx.get(IN_MESSAGE);

		if (message == null) {
			return ERROR;
		}

		MimeMessage mimeMessage = (MimeMessage) message.getPayload();

		try {
			writeMessageToSystemOut(mimeMessage);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return ERROR;
		}

		return NEXT;
	}

	/**
	 * Prints message.
	 * @param p
	 * @throws Exception
	 */
	private void writeMessageToSystemOut(Part p) throws Exception {
		if (p instanceof javax.mail.Message)
			
			writeEnvelope((javax.mail.Message) p);

		System.out.println("----------------------------");
		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writeMessageToSystemOut(mp.getBodyPart(i));
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			writeMessageToSystemOut((Part) p.getContent());
		}
		// check if the content is an inline image
		else if (p.isMimeType("image/jpeg")) {
			System.out.println("--------> image/jpeg");
			
		} else if (p.getContentType().contains("image/")) {
			System.out.println("content type" + p.getContentType());
			
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
			}
		}
	}

	/**
	 * This method prints FROM,TO and SUBJECT of the message
	 */
	private void writeEnvelope(javax.mail.Message m) throws Exception {
		System.out.println("This is the message envelope");
		System.out.println("---------------------------");
		Address[] a;

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("FROM: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(javax.mail.Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("TO: " + a[j].toString());
		}

		// SUBJECT
		if (m.getSubject() != null)
			System.out.println("SUBJECT: " + m.getSubject());

	}

}
