/**
 * 
 */
package org.neuro4j.workflow.guice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.guice.service.MessageService;
import org.neuro4j.workflow.guice.service.MyMessageService;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

/**
 *
 *
 */
public class App {

    public static void main(String[] args) {
        
        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {
                bind(MessageService.class).to(MyMessageService.class);
            }

        };

        List<Module> modules = new ArrayList<Module>();
        modules.add(module);
        
    	WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withCustomBlockInitStrategy(new GuiceCustomBlockInitStrategy(modules)));
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", "Mister");

        ExecutionResult result = engine.execute("org.neuro4j.workflow.guice.flows.Flow-Start", parameters);

        if (result.getException() == null) {
            String message = (String) result.getFlowContext().get("message");
            System.out.println("Message: " + message);

        } else {
            result.print();
        }

	}

}
