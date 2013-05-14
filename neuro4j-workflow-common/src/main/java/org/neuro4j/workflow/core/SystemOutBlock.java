package org.neuro4j.workflow.core;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={@ParameterDefinition(name="varToPrint", isOptional = true, type="java.lang.Object")})
public class SystemOutBlock extends CustomBlock {

	final static String VAR_TO_PRINT = "varToPrint";

	public int execute(LogicContext ctx) throws FlowExecutionException {
		
		Object mName = (Object)ctx.get(VAR_TO_PRINT);
		
		if (mName != null)
		{
			System.out.println(mName.toString());
		}
	
		return NEXT;
	}
	
	
	

}
