/*
 * Copyright (c) 2013-2016, Neuro4j
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

package org.neuro4j.jasper.parameters;

import static org.neuro4j.jasper.parameters.CreateReportParameters.IN_INPUTMAP;
import static org.neuro4j.jasper.parameters.CreateReportParameters.OUT_PARAMETERS;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = {
        @ParameterDefinition(name = IN_INPUTMAP, isOptional = true, type = "java.util.Map") },
        output = {
                @ParameterDefinition(name = OUT_PARAMETERS, isOptional = false, type = "java.util.Map") })
public class CreateReportParameters extends CustomBlock {

    static final String IN_INPUTMAP = "inputMap";

    static final String OUT_PARAMETERS = "parameters";

    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {

        Map inputMap = (Map) ctx.get(IN_INPUTMAP);

        Map<String, Object> parameters = new HashMap<String, Object>();

        if (inputMap != null)
        {
            parameters.putAll(inputMap);
        }

        ctx.put(OUT_PARAMETERS, parameters);

        return NEXT;
    }

    @Override
    public void init() throws FlowInitializationException {
        super.init();
    }

}
