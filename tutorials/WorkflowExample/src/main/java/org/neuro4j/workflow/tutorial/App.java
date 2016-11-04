package org.neuro4j.workflow.tutorial;

import java.util.HashMap;
import java.util.Map;

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
    	
    	// create engine with default configuration
    	WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder());
    	
    	// put input parameters
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("name", "Workflow");

    	//execute flow
    	ExecutionResult result =  engine.execute("org.neuro4j.workflow.tutorial.HelloFlow-Start", parameters);
    	
    	if (result.getException() == null)
    	{
    		String message = (String) result.getFlowContext().get("message");
    		System.out.println("Message: " + message);
    		
    	} else {
    		result.print();
    	}

    }
}
