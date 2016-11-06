package org.neuro4j.workflow.core;


import static org.neuro4j.workflow.core.CustomBlockWithErrorExit.IN_PARAM;

import java.util.concurrent.atomic.AtomicInteger;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;


@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_PARAM, isOptional=true, type= "java.lang.String")},
                         output={
                         	        })
public class CustomBlockWithErrorExit implements ActionBlock {
    
    static final String IN_PARAM = "param";
      
    
  private final AtomicInteger executeCounter = new AtomicInteger(); 
  
  private final AtomicInteger initCounter = new AtomicInteger(); 
    
    
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
    	executeCounter.getAndIncrement();
        
		
		//TODO: put your code here
		
        if (/*error != */true)
        {
        	return ERROR;
        }
		
		
		
		return NEXT;
	}
	
    @Override
    public void init() throws FlowExecutionException {
    	initCounter.getAndIncrement();
    }

	public AtomicInteger getExecuteCounter() {
		return executeCounter;
	}

	public AtomicInteger getInitCounter() {
		return initCounter;
	}
	
    

}
