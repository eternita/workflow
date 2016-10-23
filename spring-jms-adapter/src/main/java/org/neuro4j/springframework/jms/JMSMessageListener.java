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

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.neuro4j.workflow.common.WorkflowEngine;
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
    WorkflowEngine engine;
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
