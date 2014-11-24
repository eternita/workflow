
package org.neuro4j.springframework.jms.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

import org.neuro4j.springframework.context.WorkflowBeanFactoryPostProcessor;
import org.neuro4j.springframework.jms.JMSMessageListener;
import org.neuro4j.springframework.jms.JMSQueueSender;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.TriggerNodeFactory;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.util.FileSystemUtils;

@Configuration
@EnableAutoConfiguration
public class Application {

    static String mailboxDestination = "mailbox-destination";

    
    @Bean
    JMSMessageListener receiver(ConfigurableApplicationContext context) throws FlowInitializationException {    	
        return  (JMSMessageListener) TriggerNodeFactory.initTriggerNode("org.neuro4j.springframework.jms.flows.MessageFlow-JMSMessageListener");

    }
    
    @Bean
   static WorkflowBeanFactoryPostProcessor getWorkflowBeanFactoryPostProcessor(ConfigurableApplicationContext context)
    {
    	WorkflowBeanFactoryPostProcessor strategy = new WorkflowBeanFactoryPostProcessor(context);        
    	return strategy;
    }

    @Bean
    JMSQueueSender sender(ConnectionFactory connectionFactory) {    	
    	JMSQueueSender sender =  new JMSQueueSender();
    	sender.setConnectionFactory(connectionFactory);
       return sender;
    }

    @Bean
    SimpleMessageListenerContainer container(MessageListener messageListener,
                                             ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setMessageListener(messageListener);
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName(mailboxDestination);
        return container;
    }

    public static void main(String[] args) {
        // Clean out any ActiveMQ data from a previous run
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("messageDestination", mailboxDestination);
        

        
        WorkflowEngine.run("org.neuro4j.springframework.jms.flows.MessageFlow-SendMessage", parameters);


        
    }

}
