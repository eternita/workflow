
package org.neuro4j.springframework.jms.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

import org.neuro4j.springframework.context.SpringContextInitStrategy;
import org.neuro4j.springframework.jms.JMSMessageListener;
import org.neuro4j.springframework.jms.JMSQueueSender;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.Neuro4jEngine;
import org.neuro4j.workflow.common.Neuro4jEngine.ConfigBuilder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
        return new JMSMessageListener("org.neuro4j.springframework.jms.flows.MessageFlow-JMSMessageListener");
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
    
    @Bean
    Neuro4jEngine getWorkflowEngine(ConfigurableListableBeanFactory beanFactory){
    	Neuro4jEngine engine = new Neuro4jEngine(new ConfigBuilder().withCustomBlockInitStrategy(new SpringContextInitStrategy(beanFactory)));
    	return engine;
    }

    public static void main(String[] args) {

        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("messageDestination", mailboxDestination);
        parameters.put("message", "Hi Mister!");      
        
        Neuro4jEngine engine = context.getBean(Neuro4jEngine.class);
        
        engine.execute("org.neuro4j.springframework.jms.flows.MessageFlow-SendMessage", parameters);


        
    }

}
