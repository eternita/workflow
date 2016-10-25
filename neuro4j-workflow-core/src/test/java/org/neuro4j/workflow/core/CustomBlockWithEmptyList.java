package org.neuro4j.workflow.core;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinitionList;

@ParameterDefinitionList
public class CustomBlockWithEmptyList implements ActionBlock {

	public int execute(FlowContext ctx) throws FlowExecutionException {

		return NEXT;
	}

}
