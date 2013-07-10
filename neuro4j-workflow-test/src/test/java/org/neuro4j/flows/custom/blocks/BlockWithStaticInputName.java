package org.neuro4j.flows.custom.blocks;

import static org.neuro4j.flows.custom.blocks.BlockWithStaticInputName.IN_NAME1;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={@ParameterDefinition(name=IN_NAME1, isOptional=true, type= "java.lang.String")},output={ })	
public class BlockWithStaticInputName extends CustomBlock {
    
    static final String IN_NAME1 = "name1";
      
    

	public int execute(LogicContext ctx) throws FlowExecutionException {
		
		Object name1 = ctx.get(IN_NAME1);
        
		
		//TODO: put your code here
		
        if (/*error != */false)
        {
        	return ERROR;
        }
		
		
		
		return NEXT;
	}

}
