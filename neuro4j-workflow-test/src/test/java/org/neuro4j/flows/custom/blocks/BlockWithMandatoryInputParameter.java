package org.neuro4j.flows.custom.blocks;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;


@ParameterDefinitionList(input={@ParameterDefinition(name="mandatoryParameter", isOptional = false, type="java.lang.String")}, output={})
public class BlockWithMandatoryInputParameter extends CustomBlock {

	
	public int execute(LogicContext ctx)
			throws FlowExecutionException {
            System.out.println(getClass().getCanonicalName());
            return NEXT;
	}

}
