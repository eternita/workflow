package org.neuro4j.workflow.demo.lesson1;


import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;
import static org.neuro4j.workflow.demo.lesson1.HelloBlock.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_NAME, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=false, type= "java.lang.String")})	
public class HelloBlock extends CustomBlock {
    
    static final String IN_NAME = "name";
      
    static final String OUT_MESSAGE = "message"; 
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
		String name = (String)ctx.get(IN_NAME);
        
		String message = "Hello ";
		
        if (name != null)
        {
        	message += name;
        }
		
		ctx.put(OUT_MESSAGE, message); 
    
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
