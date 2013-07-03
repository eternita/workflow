package org.neuro4j.workflow.demo.lesson3;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("var", "true");
			
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.workflow.demo.lesson3.Decision-Start", params);
			

		} catch (FlowExecutionException e) {
			e.printStackTrace();
		}

	}
	


	
}
