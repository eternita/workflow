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

package org.neuro4j.workflow.node;

import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.enums.StartNodeTypes;

public class StartNode extends WorkflowNode {

    StartNodeTypes type;
    String flowPackage;

    Transition nextNode = null;

    private static final String NEXT = "NEXT";

    public StartNode(String name, String uuid, Workflow workflow)
    {
        super(name, uuid, workflow);
        workflow.registerStartNode(this);
    }

    public void setType(StartNodeTypes type) {
        this.type = type;
    }

    public boolean isPublic()
    {
        return type == StartNodeTypes.PUBLIC;
    }

    public String getPackage()
    {
        return workflow.getPackage();
    }



    @Override
    public Transition execute(final WorkflowProcessor processor, WorkflowRequest request) throws FlowExecutionException {
        request.setNextRelation(nextNode);
        return nextNode;
    }

    @Override
    public void init() throws FlowInitializationException {
        super.init();
        nextNode = getExitByName(NEXT);
    }

}
