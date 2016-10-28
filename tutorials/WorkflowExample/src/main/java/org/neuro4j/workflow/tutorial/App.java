package org.neuro4j.workflow.tutorial;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;

/**
 * This is client's class.
 */
public class App 
{

	
    public static void main( String[] args )
    {
    	
    	WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder());
    	
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("name", "Workflow");

    	ExecutionResult result =  engine.execute("org.neuro4j.workflow.tutorial.HelloFlow-Start", parameters);
    	
    	if (result.getException() == null)
    	{
    		String message = (String) result.getFlowContext().get("message");
    		System.out.println("Message: " + message);
    		
    	} else {
    		result.print();
    	}
    	
    	// run 10 times
    	for (int i=0; i < 10 ; i++) {
        	parameters = new HashMap<String, Object>();
        	parameters.put("name", UUID.randomUUID().toString());
    		engine.execute("org.neuro4j.workflow.tutorial.HelloFlow-Start", parameters);
    	}
    }
}
