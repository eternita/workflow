package org.neuro4j.springframework.jms;

import static org.neuro4j.springframework.jms.CloseApplicationContext.IN_APPCONTEXT;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Close application Context.
 *
 */
@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_APPCONTEXT, isOptional = true, type = "org.springframework.context.ConfigurableApplicationContext") }, output = {})
public class CloseApplicationContext extends CustomBlock {

	static final String IN_APPCONTEXT = "appContext";

	public int execute(FlowContext ctx) throws FlowExecutionException {

		ConfigurableApplicationContext appContext = (ConfigurableApplicationContext) ctx
				.get(IN_APPCONTEXT);

		if (appContext != null) {
			appContext.close();
		}
		
		 
		return NEXT;
	}


}
