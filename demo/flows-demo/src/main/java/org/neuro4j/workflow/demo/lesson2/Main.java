package org.neuro4j.workflow.demo.lesson2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.WorkflowEngine;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

			Map<String, Object> params = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			list.add("value1");
			list.add("value2");
			list.add("value3");
			
			
			params.put("list1", list);
			
			ExecutionResult result = new WorkflowEngine().execute("org.neuro4j.workflow.demo.lesson2.Loop-Start", params);
			


	}
	


	
}
