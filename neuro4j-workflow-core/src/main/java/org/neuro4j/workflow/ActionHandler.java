package org.neuro4j.workflow;

import org.neuro4j.workflow.node.WorkflowNode.NodeInfo;

public interface ActionHandler {
	
	public default void preExecute(NodeInfo nodeInfo, FlowContext context, ActionBlock actionBlock){
		
	}
	
	public default void postExecute(NodeInfo nodeInfo, FlowContext context, ActionBlock actionBlock){
		
	}

}
