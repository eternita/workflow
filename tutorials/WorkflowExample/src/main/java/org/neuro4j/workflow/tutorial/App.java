package org.neuro4j.workflow.tutorial;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowEngine;

/**
 * This is client's class.
 */
public class App 
{
    public static void main( String[] args )
    {
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("name", "Workflow");
    	
    	ExecutionResult result =  WorkflowEngine.run("org.neuro4j.workflow.tutorial.HelloFlow-Start", parameters);
    	
    	if (result.getException() == null)
    	{
    		String message = (String) result.getFlowContext().get("message");
    		System.out.println("Message: " + message);
    		
    	} else {
    		result.print();
    	}
    }
}
