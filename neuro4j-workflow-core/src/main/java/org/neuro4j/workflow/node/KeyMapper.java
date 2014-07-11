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

package org.neuro4j.workflow.node;

import java.util.Set;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.SWEUtils;
import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.loader.n4j.SWFConstants;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * KeyMapper maps value from flow context to different name.
 *
 */
public class KeyMapper extends LogicBlock {
    WorkflowNode node = null;
    private Transition nextNode = null;

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public final Transition execute(WorkflowRequest request)
            throws FlowExecutionException {
        FlowContext ctx = request.getLogicContext();
        Set<String> parameterKeys = node.getParameters().keySet();

        for (String key : parameterKeys)
        {
            if (key.startsWith(SWFParametersConstants.MAPPER_NODE_KEY_PREFIX))
            {
                String mappedValue = node.getParameter(key);
                if (mappedValue != null)
                {
                    String[] splittedValue = SWEUtils.getMappedParameters(mappedValue);
                    evaluateParameterValue(splittedValue[0], splittedValue[1], ctx);
                }

            }
        }

        request.setNextRelation(nextNode);

        return nextNode;
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode node) throws FlowInitializationException {
        this.node = node;
        nextNode = node.getExitByName(SWFConstants.NEXT_RELATION_NAME);
    }

}
