package org.neuro4j.springintegration.mail;

import org.neuro4j.springframework.context.WorkflowBeanFactoryPostProcessor;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.TriggerBlock;
import org.neuro4j.workflow.common.TriggerNodeFactory;
import org.neuro4j.workflow.log.Logger;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageHandler;

/**
 * This class creates trigger node and subscribes to inputChannel 
 *
 */
public class MessageHandlerFactoryBean extends WorkflowBeanFactoryPostProcessor {

	/**
	 * The constructor.
	 */
	public MessageHandlerFactoryBean() {
	}

	/**
	 * Creates and subscribes trigger node.
	 * 
	 * @param inputChannel
	 * @param flow trigger path (eg. org.neuro4j.MyFlow-MyTriggerNode)
	 * @return MessageHandler object.
	 * @throws FlowInitializationException
	 */
	public MessageHandler initMessageHandler(DirectChannel inputChannel, String flow) throws FlowInitializationException{
		
		if (inputChannel == null){
			throw new FlowInitializationException("inputChannel is NULL");
		}

		TriggerBlock trigger = TriggerNodeFactory.initTriggerNode(flow);
		
		if (trigger instanceof MessageHandler) {
			MessageHandler	messageHandler = (MessageHandler) trigger;
			inputChannel.subscribe(messageHandler);
			return messageHandler;
		}
        Logger.error(this, "Wrong triggerNode: " + flow);
        
		throw new FlowInitializationException("TriggerNode can not be initialized");
	}

}
