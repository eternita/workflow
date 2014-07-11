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

package org.neuro4j.workflow.common;

import java.util.Collections;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.WorkflowMngImpl;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.xml.StartNode;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * 
 * Runs flows stored on file system in XML files (.n4j extension)
 * 
 */
public class WorkflowEngine {

    private static final String FLOW_FILE_EXTENSION = ".n4j";

    public static ExecutionResult run(String flow) {
        return run(flow, Collections.EMPTY_MAP);
    }

    public static ExecutionResult run(String flow, Map<String, Object> params) {
        WorkflowRequest request = new WorkflowRequest();

        if (null != params && !params.isEmpty()) {
            for (String key : params.keySet())
                request.addParameter(key, params.get(key));
        }
        return run(flow, request);
    }

    /**
     * 
     * @param flow
     *        - should be like package.name.FlowName-StartNode
     * @param params
     * @return
     * @throws SimpleWorkflowException
     *         - if can't load flow, can't find start node, etc
     */
    public static ExecutionResult run(String flow, WorkflowRequest request) {

        ExecutionResult result = new ExecutionResult(request.getLogicContext());

        try {
            String[] fArr = parseFlowName(flow);
            String flowName = fArr[0];
            String startNode = fArr[1];

            Logger.debug(WorkflowEngine.class, "Loading flow: {}.",
                    flowName);

            Workflow workflow = loadFlow(flowName);

            if (null == workflow)
                throw new FlowExecutionException("Flow '" + flowName + "' can't be loaded");

            Logger.debug(WorkflowEngine.class, "Loaded flow: {}.", flowName);

            StartNode startNodeAdapter = workflow.getStartNode(startNode);
            if (null == startNodeAdapter)
                throw new FlowExecutionException("StartNode '" + startNode
                        + "' not found in flow " + flowName);

            if (!workflow.isPublic()) {
                throw new FlowExecutionException("Flow '" + flow
                        + "' is not public");
            }

            if (!startNodeAdapter.isPublic()) {
                throw new FlowExecutionException("Node '"
                        + startNodeAdapter.getName() + "' is not public");
            }

            request.pushPackage(workflow.getPackage());

            workflow.executeWorkflow(startNodeAdapter, request);
            request.popPackage();
        } catch (FlowExecutionException ex) {
            Logger.error(FlowExecutionException.class, ex.getMessage(), ex);
            result.setExecutionExeption(ex);
        }

        WorkflowNode lastNode = request.getLastSuccessfulNode();
        if (lastNode != null)
        {
            result.setLastSuccessfulNodeName(lastNode.getName());
        }

        return result;
    }

    public static String[] parseFlowName(String flow)
            throws FlowExecutionException {

        int separatorIdx = flow.indexOf("-");
        if (-1 == separatorIdx)
            throw new FlowExecutionException(
                    "Incorrect flow name. Must be package.name.FlowName-StartNode");
        // check > 1 "-"
        if (separatorIdx != flow.lastIndexOf("-"))
            throw new FlowExecutionException("Incorrect flow name. Must be package.name.FlowName-StartNode");

        String flowName = flow.substring(0, separatorIdx);
        // replace '.' -> '/'
        if (-1 < flowName.indexOf("."))
            flowName = flowName.replace('.', '/');

        String startNode = flow.substring(separatorIdx + 1);

        return new String[] { flowName, startNode };
    }

    /**
     * Get flow by name. Should be without .n4j extension. E.g.
     * package.name.FlowName
     * 
     * @param flowName
     * @return
     */
    public static Workflow loadFlow(String flowName) {
        try {
            return WorkflowMngImpl.getInstance().lookupWorkflow(flowName);
        } catch (FlowInitializationException e) {
            Logger.error(WorkflowEngine.class, e.getMessage(), e);
        }
        return null;
    }

}
