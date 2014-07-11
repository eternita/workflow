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

import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * StartBlock starts flow execution.
 *
 */
public final class StartBlock extends LogicBlock {

    Transition nextNode = null;

    private static final String NEXT = "NEXT";

    /**
     * Default constructor.
     */
    public StartBlock() {
        super();
    }

    /**
     * Constructor.
     * @param name
     */
    public StartBlock(String name) {
        super();
        setName(name);
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode descriptor) throws FlowInitializationException {
        nextNode = descriptor.getExitByName(NEXT);
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public final Transition execute(WorkflowRequest request) throws FlowExecutionException {
        request.setNextRelation(nextNode);
        return nextNode;
    }

}
