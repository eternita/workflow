package org.neuro4j.workflow.guice.flows;

import static org.neuro4j.workflow.guice.flows.CustomBlockWithService.IN_NAME;
import static org.neuro4j.workflow.guice.flows.CustomBlockWithService.OUT_MESSAGE;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.guice.service.MessageService;

import com.google.inject.Inject;

/**
 * This custom block uses Google Guice library for dependency injection.
 * 
 */

@ParameterDefinitionList(
		                 input = { @ParameterDefinition(name = IN_NAME, isOptional = true, type = "java.lang.String") },
                         output = { @ParameterDefinition(name = OUT_MESSAGE, isOptional = true, type = "java.lang.String") })
public class CustomBlockWithService implements ActionBlock {

	static final String IN_NAME = "name";

	static final String OUT_MESSAGE = "message";

	private MessageService service;

	public int execute(FlowContext ctx) throws FlowExecutionException {

		String name = (String) ctx.get(IN_NAME);

		String message = null;

		if (name != null) {
			message = this.service.sendMessage("Hi", name);
		}

		ctx.put(OUT_MESSAGE, message);
		return NEXT;
	}

	@Inject
	public void setService(MessageService svc) {
		this.service = svc;
	}

}
