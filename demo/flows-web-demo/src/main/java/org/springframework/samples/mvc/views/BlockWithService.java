package org.springframework.samples.mvc.views;


import static org.springframework.samples.mvc.views.BlockWithService.IN_FOO;
import static org.springframework.samples.mvc.views.BlockWithService.OUT_MESSAGE;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.mvc.service.UserService;
import org.springframework.stereotype.Component;


@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_FOO, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=true, type= "java.lang.String")})	

@Component
public class BlockWithService extends CustomBlock {
    
    static final String IN_FOO = "foo";
    
    static final String OUT_MESSAGE = "message"; 
	
    // Spring will use dependency injection to initialize service...
    @Autowired  
    private UserService userService;
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
    	
    	String formBean = (String) ctx.get(IN_FOO);
        
    	if (formBean == null){
    		return ERROR;
    	}
		
    	// your business code...
    	
		String message = userService.serve();
		
		
		ctx.put(OUT_MESSAGE, message); 
	
		return NEXT;
	}
	
	

}
