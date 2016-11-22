/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.neuro4j.springframework.jms;

import static org.neuro4j.springframework.jms.CloseApplicationContext.IN_APPCONTEXT;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Close application Context.
 *
 */
@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_APPCONTEXT, isOptional = true, type = "org.springframework.context.ConfigurableApplicationContext") }, output = {})
public class CloseApplicationContext implements ActionBlock {

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
