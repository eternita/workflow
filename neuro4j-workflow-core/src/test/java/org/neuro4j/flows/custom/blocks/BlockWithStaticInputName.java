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

package org.neuro4j.flows.custom.blocks;

import static org.neuro4j.flows.custom.blocks.BlockWithStaticInputName.IN_NAME1;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = { @ParameterDefinition(name = IN_NAME1, isOptional = true, type = "java.lang.String") }, output = { })
public class BlockWithStaticInputName extends CustomBlock {

    static final String IN_NAME1 = "name1";

    public int execute(FlowContext ctx) throws FlowExecutionException {

        Object name1 = ctx.get(IN_NAME1);

        // TODO: put your code here

        if (/* error != */false)
        {
            return ERROR;
        }

        return NEXT;
    }

}
