package org.neuro4j.workflow;

public interface ActionHandler {
	
	public default void preExecute(ActionBlock actionBlock, FlowContext context){
		
	}
	
	public default void postExecute(ActionBlock actionBlock, FlowContext context){
		
	}

}
