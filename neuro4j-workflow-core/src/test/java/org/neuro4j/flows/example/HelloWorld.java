package org.neuro4j.flows.example;

import static org.neuro4j.flows.example.HelloWorld.IN_NAME;
import static org.neuro4j.flows.example.HelloWorld.OUT_MESSAGE;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import static org.neuro4j.workflow.enums.ActionBlockCache.*;
import org.neuro4j.workflow.enums.CachedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  HelloWorld block receives name as input parameter and returns message as output parameters.
 *  
 */
@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_NAME, isOptional = true, type = "java.lang.String") }, 
                         output = { @ParameterDefinition(name = OUT_MESSAGE, isOptional = true, type = "java.lang.String") })

// will create  just one instance  of HelloWorld's class in workflow
@CachedNode(type=SINGLETON)
public class HelloWorld implements ActionBlock {
	
	private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

	static final String IN_NAME = "name";

	static final String OUT_MESSAGE = "message";

	public int execute(FlowContext ctx) throws FlowExecutionException {

		String name = (String) ctx.get(IN_NAME);

		String message = "Hello World! ";

		if (name != null) {
			message += name;
		}
		
		logger.debug("Message: {}", message);
		
		ctx.put(OUT_MESSAGE, message);

		return NEXT;
	}


}
