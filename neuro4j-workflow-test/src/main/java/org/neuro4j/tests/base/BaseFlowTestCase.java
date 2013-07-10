package org.neuro4j.tests.base;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;

public class BaseFlowTestCase {
	
	private static LogicContext EMPTY_CONTEXT = new LogicContext();
	
	protected LogicContext executeFlow(String flowName, Map<String, Object> parameters) throws FlowExecutionException
	{
		LogicContext	logicContext = SimpleWorkflowEngine.run(flowName, parameters);

		return logicContext;
	}
	
	protected LogicContext executeFlow(String flowName) throws FlowExecutionException
	{
		return executeFlow(flowName, new HashMap<String, Object>());
	}
	
	protected Object executeFlowAndReturnObject(String flowName, String objectName) throws FlowExecutionException
	{
		LogicContext logicContext = executeFlow(flowName, new HashMap<String, Object>());
		
		return logicContext.get(objectName);
	}
	
	protected Object executeFlowAndReturnObject(String flowName, Map<String, Object> parameters, String objectName) throws FlowExecutionException
	{
		LogicContext logicContext = executeFlow(flowName, parameters);
		
		return logicContext.get(objectName);
	}

}
