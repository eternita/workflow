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

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.loader.n4j.SWFConstants;
import org.neuro4j.workflow.xml.SwitchNode;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * SwitchNode dispatches flow execution according to value in flow context.
 *
 */
public final class SwitchBlock extends LogicBlock {

    private String relationName = null;
    private Transition defaultRelation = null;
    private SwitchNode node = null;

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    public final void validate(FlowContext fctx) throws FlowExecutionException
    {
        if (relationName == null && defaultRelation == null)
        {
            throw new FlowExecutionException("Switch node has wrong configuration.");
        }

    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public Transition execute(WorkflowRequest request)
            throws FlowExecutionException {
        FlowContext ctx = request.getLogicContext();
        Transition nextStepUUID = null;
        String relation = relationName;

        if (relationName != null && !relationName.startsWith(SWFConstants.QUOTES_SYMBOL))
        {
            relation = (String) ctx.get(relationName);
        } else if (relationName != null) {
            relation = relation.replace(SWFConstants.QUOTES_SYMBOL, "");
        }

        if (null == relation)
            relation = "null";

        nextStepUUID = node.getExitByName(relation);

        if (nextStepUUID == null && defaultRelation != null)
        {
            nextStepUUID = defaultRelation;
        }

        if (nextStepUUID != null)
        {
            request.setNextRelation(nextStepUUID);

        } else {
            throw new FlowExecutionException("Switch: NextStep is unknown.");
        }

        return nextStepUUID;
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode workflowNode) throws FlowInitializationException
    {
        node = (SwitchNode) workflowNode;
        relationName = node.getRelationName();

        defaultRelation = node.getExitByName(SWFParametersConstants.SWITCH_NODE_DEFAULT_ACTION_NAME_2);
        if (defaultRelation == null)
        {
            defaultRelation = node.getExitByName(SWFParametersConstants.SWITCH_NODE_DEFAULT_ACTION_NAME);
        }
        return;
    }

}
