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
import org.neuro4j.workflow.loader.n4j.SWFConstants;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * JoinBlock links different blocks. Block has many 'input' transitions and one 'output' transition.
 */
public class JoinBlock extends LogicBlock {

    private Transition next = null;

    /**
     * Constructor.
     * 
     * @param entity
     */
    public JoinBlock(WorkflowNode entity)
    {
        this();
    }

    /**
     * Default constructor.
     */
    public JoinBlock()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public final Transition execute(WorkflowRequest request)
            throws FlowExecutionException {
        request.setNextRelation(next);
        return next;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode entity) throws FlowInitializationException {
        next = entity.getExitByName(SWFConstants.NEXT_RELATION_NAME);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    @Override
    public final void validate(FlowContext ctx) throws FlowExecutionException {
        super.validate(ctx);

        if (next == null)
        {
            throw new FlowExecutionException("JoinBlock: Wrong configuration");
        }

    }

}
