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

package org.neuro4j.workflow.core;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = { @ParameterDefinition(name = "varToPrint", isOptional = true, type = "java.lang.Object") })
public class SystemOutBlock extends CustomBlock {

    final static String VAR_TO_PRINT = "varToPrint";

    public int execute(FlowContext ctx) throws FlowExecutionException {

        Object mName = (Object) ctx.get(VAR_TO_PRINT);

        if (mName != null)
        {
            System.out.println(mName.toString());
        }

        return NEXT;
    }

}
