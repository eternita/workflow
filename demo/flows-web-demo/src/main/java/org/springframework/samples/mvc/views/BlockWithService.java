package org.springframework.samples.mvc.views;


import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.mvc.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import static org.springframework.samples.mvc.views.BlockWithService.*;

@ParameterDefinitionList(input={
                                	},
                         output={
                       	        })
@Component
public class BlockWithService extends CustomBlock {
    
    @Autowired  
    private UserService userService;
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		
    	userService.serve();
		
		
		return NEXT;
	}
	
	@Override
	public void init() throws FlowInitializationException{
		super.init();
	}
	

}
