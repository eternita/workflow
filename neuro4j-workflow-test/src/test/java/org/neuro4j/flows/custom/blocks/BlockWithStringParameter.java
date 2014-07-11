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

package org.neuro4j.flows.custom.blocks;

import static org.neuro4j.flows.custom.blocks.BlockWithStringParameter.IN_STRING1;
import static org.neuro4j.flows.custom.blocks.BlockWithStringParameter.IN_STRING2;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = {
        @ParameterDefinition(name = IN_STRING1, isOptional = true, type = "java.lang.String") },
        output = {
                @ParameterDefinition(name = IN_STRING2, isOptional = true, type = "java.lang.String") })
public class BlockWithStringParameter extends CustomBlock {

    static final String IN_STRING1 = "string1";
    static final String IN_STRING2 = "string2";

    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {

        String string1 = (String) ctx.get(IN_STRING1);

        // TODO: put your code here

        if (string1 == null)
        {
            return ERROR;
        }

        ctx.put(IN_STRING2, string1);

        return NEXT;
    }

    @Override
    protected void init() throws FlowInitializationException {
        super.init();
    }

}
