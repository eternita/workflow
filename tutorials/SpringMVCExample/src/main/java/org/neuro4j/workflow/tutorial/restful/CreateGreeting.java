package org.neuro4j.workflow.tutorial.restful;

import static org.neuro4j.workflow.tutorial.restful.CreateGreeting.IN_NAME;
import static org.neuro4j.workflow.tutorial.restful.CreateGreeting.OUT_MESSAGE;

import java.util.concurrent.atomic.AtomicLong;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_NAME, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=true, type= "org.neuro4j.workflow.tutorial.restful.Greeting")})	
public class CreateGreeting extends CustomBlock {
    
    static final String IN_NAME = "name";
      
    static final String OUT_MESSAGE = "message"; 
    private final AtomicLong counter = new AtomicLong();
    
    
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		String name = (String)ctx.get(IN_NAME);
        
		
		Greeting greeting = new Greeting(counter.incrementAndGet(), name);
		
		
		ctx.put(OUT_MESSAGE, greeting); 
    
		
		return NEXT;
	}
	
	

}
