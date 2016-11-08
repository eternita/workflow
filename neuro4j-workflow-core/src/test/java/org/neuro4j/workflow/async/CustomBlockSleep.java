package org.neuro4j.workflow.async;


import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.enums.CachedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.neuro4j.workflow.ActionBlock;
import static org.neuro4j.workflow.enums.ActionBlockCache.*;
import static org.neuro4j.workflow.async.CustomBlockSleep.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_TIMETOSLEE, isOptional=true, type= "java.lang.Integer")},
                         output={
                         	        })	
@CachedNode(type=NONE)                         	       
public class CustomBlockSleep implements ActionBlock {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomBlockSleep.class);
    
	static final String IN_TIMETOSLEE = "timeToSlee";
      
    
    
    
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		Integer timeToSlee = (Integer)ctx.get(IN_TIMETOSLEE);
        
		try {
			Thread.sleep(timeToSlee);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		
        if (/*error != */false)
        {
        	return ERROR;
        }
		
		
		
		return NEXT;
	}
	
	

}
