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
package org.neuro4j.workflow.node;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import static org.neuro4j.workflow.common.SWFParametersConstants.*;
import org.neuro4j.workflow.loader.f4j.SWFConstants;

public class SwitchNode extends WorkflowNode {

    private final String relationName;
    private Transition defaultRelation = null;

    public SwitchNode(String name, String uuid, String relationName)
    {
        super(name, uuid);
        this.relationName = relationName;
    }

    public String getRelationName() {
        return relationName;
    }


    @Override
    public final Transition execute(WorkflowProcessor processor, WorkflowRequest request)  throws FlowExecutionException {
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

        nextStepUUID = getExitByName(relation);

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

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    @Override
    public final void init() throws FlowExecutionException {

        defaultRelation = getExitByName(SWITCH_NODE_DEFAULT_ACTION_NAME_2);
        if (defaultRelation == null)
        {
            defaultRelation = getExitByName(SWITCH_NODE_DEFAULT_ACTION_NAME);
        }
        return;
    }

}
