package org.neuro4j.example.web.blocks.msg;

import static org.neuro4j.example.web.blocks.msg.SetMessage.IN_TEXTKEY;
import static org.neuro4j.example.web.blocks.msg.SetMessage.OUT_MESSAGE;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_TEXTKEY, isOptional = true, type = "java.lang.String") }, output = { @ParameterDefinition(name = OUT_MESSAGE, isOptional = true, type = "java.lang.String") })
public class SetMessage extends CustomBlock {

	private static final String FILE_NAME = "languages/messages";

	ResourceBundle resourceBundle = null;

	static final String IN_TEXTKEY = "textKey";

	static final String OUT_MESSAGE = "message";

	public int execute(LogicContext ctx) throws FlowExecutionException {

		String textKey = (String) ctx.get(IN_TEXTKEY);
		String message = textKey;
		if (textKey != null) {
			try {
				message = resourceBundle.getString(textKey);
			} catch (MissingResourceException ex) {
                
			}
			ctx.put(OUT_MESSAGE, message);
		}

		return NEXT;
	}

	@Override
	protected void init() throws FlowInitializationException {
		super.init();
		resourceBundle = ResourceBundle.getBundle(FILE_NAME);
		if (resourceBundle == null) {
			throw new FlowInitializationException(
					"ResourceBundle can not be loaded. File: " + FILE_NAME);
		}
	}

}
