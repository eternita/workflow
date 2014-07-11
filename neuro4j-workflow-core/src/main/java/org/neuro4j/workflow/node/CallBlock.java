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
import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.WorkflowMngImpl;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.xml.CallNode;
import org.neuro4j.workflow.xml.StartNode;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * CallBlock calls other flow and returns result of execution.
 * 
 */
public class CallBlock extends LogicBlock {

    private String flownName = null;
    private String dynamicFlownName = null;
    private CallNode node = null;

    /**
     * Default constructor
     */
    public CallBlock() {
        super();
    }

    /**
     * Constructor
     * 
     * @param name
     *        the node's name
     */
    public CallBlock(String name) {
        super();
        setName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    final public void validate(FlowContext ctx) throws FlowExecutionException
    {

        if (dynamicFlownName == null && flownName == null)
        {
            throw new FlowExecutionException("CallByFlowName node: Flow not defined.");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public Transition execute(WorkflowRequest request) throws FlowExecutionException {
        FlowContext ctx = request.getLogicContext();
        String flow = null;
        if (dynamicFlownName != null)
        {
            flow = (String) ctx.get(dynamicFlownName);
        } else {
            flow = flownName;
        }

        if (flow == null)
        {
            throw new FlowExecutionException("CallNode: Flow not defined.");
        }

        String[] fArr = WorkflowEngine.parseFlowName(flow);
        String flowName = fArr[0];
        String startNodeName = fArr[1];

        Workflow calledWorkflow = loadFlow(flowName);
        if (calledWorkflow == null)
        {
            throw new FlowExecutionException(flowName + " not found.");
        }

        StartNode startNode = calledWorkflow.getStartNode(startNodeName);
        checkNodeBeforeCall(startNode, request);

        if (startNode == null)
        {
            throw new FlowExecutionException(new StringBuilder(startNodeName).append(" not found in flow ").append(flowName).toString());
        }

        try {

            request.pushPackage(calledWorkflow.getPackage());

            calledWorkflow.executeWorkflow(startNode, request);

            request.popPackage();
        } catch (FlowExecutionException e1) {
            request.popPackage();
            throw new FlowExecutionException(e1.getCause());
        }

        WorkflowNode endNode = request.getLastSuccessfulNode();

        Transition nextNode = node.getExitByName(endNode.getName());

        if (nextNode == null)
        {
            if (node.getExits().size() == 1) {
                nextNode = node.getExits().iterator().next();

            }
        }

        request.setNextRelation(nextNode);

        return nextNode;
    }

    /**
     * Validates if startNode is public.
     * 
     * @param startNode
     * @param request
     * @throws FlowExecutionException
     */
    private void checkNodeBeforeCall(StartNode startNode, WorkflowRequest request) throws FlowExecutionException
    {
        if (!startNode.isPublic())
        {
            if (!request.getCurrentPackage().equals(startNode.getPackage()))
            {
                throw new FlowExecutionException(new StringBuilder("Node ").append(startNode.getName()).append(" in package ").append(startNode.getPackage()).append(" is private and can be used just inside package.").toString());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode entity) throws FlowInitializationException
    {
        node = (CallNode) entity;
        flownName = node.getCallFlow();
        dynamicFlownName = node.getDynamicFlownName();

    }

    /**
     * Loads called flow from storage.
     * 
     * @param flowName
     * @return
     */
    private Workflow loadFlow(String flowName) {
        try {
            return WorkflowMngImpl.getInstance().lookupWorkflow(flowName);
        } catch (FlowInitializationException e) {
            Logger.error(this, e.getMessage(), e);
        }
        return null;
    }

}
