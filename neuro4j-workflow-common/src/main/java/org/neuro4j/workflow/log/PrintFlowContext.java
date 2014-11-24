/*
 * Copyright (c) 2013-2014, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuro4j.workflow.log;

import java.util.Map;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.CustomBlock;

/**
 * Print all parameters in FlowContext
 *
 */
@ParameterDefinitionList(input = {}, output = {})
public class PrintFlowContext extends CustomBlock {

	public int execute(FlowContext ctx) throws FlowExecutionException {

		Map<String, Object> parameters = ctx.getParameters();

		for (String key : parameters.keySet()) {
			Logger.info(this, " \n Key: {}   value: {} ", new Object[] { key, parameters.get(key) });
		}

		return NEXT;
	}

}
