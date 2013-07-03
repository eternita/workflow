package org.neuro4j.workflow.demo.lesson1;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;

public class Main {

	/**
	 * This class shows how to run simple flow with user-defined code. User Block "HelloBlock" combines input parameter "name" with text "Hello " and returns it. 
	 * After execution new variable "message" will hold this text. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", "John");
			
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.workflow.demo.lesson1.Hello-Start", params);
			
			String greeting = (String) logicContext.get("message");
			System.out.println(greeting);
		} catch (FlowExecutionException e) {
			e.printStackTrace();
		}

	}
	


	
}
