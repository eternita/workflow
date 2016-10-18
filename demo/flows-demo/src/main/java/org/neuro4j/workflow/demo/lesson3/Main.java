package org.neuro4j.workflow.demo.lesson3;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowEngine;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var", "true");
			
			ExecutionResult result = new WorkflowEngine().execute("org.neuro4j.workflow.demo.lesson3.Decision-Start", params);
			


	}
	


	
}
