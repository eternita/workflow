package org.neuro4j.workflow.demo.lesson2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
			List<String> list = new ArrayList<String>();
			list.add("value1");
			list.add("value2");
			list.add("value3");
			
			
			params.put("list1", list);
			
			LogicContext logicContext = SimpleWorkflowEngine.run("org.neuro4j.workflow.demo.lesson2.Loop-Start", params);
			

		} catch (FlowExecutionException e) {
			e.printStackTrace();
		}

	}
	


	
}
