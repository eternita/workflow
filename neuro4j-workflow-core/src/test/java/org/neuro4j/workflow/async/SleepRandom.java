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

import java.util.concurrent.ThreadLocalRandom;

import static org.neuro4j.workflow.async.SleepRandom.*;

@ParameterDefinitionList(input={
                                	},
                         output={
                         	        })	
@CachedNode(type=NONE)                         	       
public class SleepRandom implements ActionBlock {
	
	private static final Logger logger = LoggerFactory.getLogger(SleepRandom.class);
    
      
    
    
    
	public int execute(FlowContext ctx) throws FlowExecutionException {

		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(100, 2000));
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		return NEXT;
	}
	
	

}
