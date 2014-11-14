package org.neuro4j.workflow.tutorial.mvc.views;

import static org.neuro4j.workflow.tutorial.mvc.views.BlockWithService.IN_FRUIT;
import static org.neuro4j.workflow.tutorial.mvc.views.BlockWithService.OUT_MESSAGE;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.neuro4j.workflow.tutorial.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_FRUIT, isOptional = true, type = "java.lang.String") }, 
                         output = { @ParameterDefinition(name = OUT_MESSAGE, isOptional = true, type = "java.lang.String") })
@Component
public class BlockWithService extends CustomBlock {

	static final String IN_FRUIT = "fruit";

	static final String OUT_MESSAGE = "message";

	@Autowired
	private UserService userService;

	public int execute(FlowContext ctx) throws FlowExecutionException {

		String fruit = (String) ctx.get(IN_FRUIT);
		//
		if (fruit == null) {
			return ERROR;
		}

		String message = userService.serve(fruit);

		ctx.put(OUT_MESSAGE, message);

		return NEXT;
	}

	@Override
	public void init() throws FlowInitializationException {
		super.init();
	}

}
