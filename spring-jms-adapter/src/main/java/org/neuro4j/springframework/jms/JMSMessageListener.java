package org.neuro4j.springframework.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.neuro4j.workflow.common.Neuro4jEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Listen to specific destination
 *
 */
public class JMSMessageListener implements MessageListener {

    static final String OUT_MESSAGE = "message";
    static final String OUT_APPLICATION_CONTEXT = "appContext";
    private final String flow;

    public JMSMessageListener(final String flow){
    	this.flow = flow;
    }
    /**
     * Get a copy of the application context
     */
    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    Neuro4jEngine engine;
    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(OUT_MESSAGE, message);
        parameters.put(OUT_APPLICATION_CONTEXT, context);

        engine.execute(this.flow, parameters);

    }

}
