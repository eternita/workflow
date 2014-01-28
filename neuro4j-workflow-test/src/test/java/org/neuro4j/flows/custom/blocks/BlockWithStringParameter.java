package org.neuro4j.flows.custom.blocks;


import static org.neuro4j.flows.custom.blocks.BlockWithStringParameter.IN_STRING1;
import static org.neuro4j.flows.custom.blocks.BlockWithStringParameter.IN_STRING2;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_STRING1, isOptional=true, type= "java.lang.String")},
                         output={
		                            @ParameterDefinition(name=IN_STRING2, isOptional=true, type= "java.lang.String")}
)	
public class BlockWithStringParameter extends CustomBlock {
    
    static final String IN_STRING1 = "string1";
    static final String IN_STRING2 = "string2";
      
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
		String string1 = (String)ctx.get(IN_STRING1);
        
		
		//TODO: put your code here
		
        if (string1 == null)
        {
        	return ERROR;
        }
		
        ctx.put(IN_STRING2, string1);
		
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
