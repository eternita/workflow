package org.neuro4j.springframework.jms;

import static org.neuro4j.springframework.jms.JMSMessageListener.OUT_APPLICATION_CONTEXT;
import static org.neuro4j.springframework.jms.JMSMessageListener.OUT_MESSAGE;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.common.TriggerBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Listen to specific destination
 *
 */
@ParameterDefinitionList(input = { },
        output = { @ParameterDefinition(name = OUT_MESSAGE, isOptional = true, type = "javax.jms.Message"),
		           @ParameterDefinition(name = OUT_APPLICATION_CONTEXT, isOptional = true, type = "org.springframework.context.ApplicationContext")})
public class JMSMessageListener extends TriggerBlock implements MessageListener {

    static final String OUT_MESSAGE = "message";
    static final String OUT_APPLICATION_CONTEXT = "appContext";

    /**
     * Get a copy of the application context
     */
    @Autowired
    ConfigurableApplicationContext context;

    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(OUT_MESSAGE, message);
        parameters.put(OUT_APPLICATION_CONTEXT, context);

        executeFlow(parameters);

    }

}
