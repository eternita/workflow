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

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.CustomBlockLoader;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.node.WorkflowNode;

/**
 * 
 * Runs flows stored on file system in XML files (.n4j extension)
 * 
 */
public class WorkflowEngine {


    public static ExecutionResult run(String flow) {
        return run(flow, new HashMap<String, Object>());
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
        long start = System.currentTimeMillis();
        ExecutionResult result = new ExecutionResult(request.getLogicContext());

        try {
            String[] array = parseFlowName(flow);
            String flowName = array[0];
            String startNodeName = array[1];

            Logger.debug(WorkflowEngine.class, "Loading flow: {}.",
                    flowName);

            Workflow workflow = loadFlow(flowName);

            if (null == workflow)
                throw new FlowExecutionException("Flow '" + flowName + "' can't be loaded");

            Logger.debug(WorkflowEngine.class, "Loaded flow: {}.", flowName);

            StartNode startNode = workflow.getStartNode(startNodeName);
            if (null == startNode)
                throw new FlowExecutionException("StartNode '" + startNodeName  + "' not found in flow " + flowName);

            if (!workflow.isPublic()) {
                throw new FlowExecutionException("Flow '" + flow + "' is not public");
            }

            if (!startNode.isPublic()) {
                throw new FlowExecutionException("Node '" + startNode.getName() + "' is not public");
            }

            request.pushPackage(workflow.getPackage());

            workflow.executeWorkflow(startNode, request);
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
        Logger.debug(WorkflowEngine.class, "Flow execution time: {} ms.", System.currentTimeMillis() - start);
        return result;
    }

    /**
     * Start flow execution from trigger node.
     * @param trigger
     * @param request
     * @return Execution result
     */
    static ExecutionResult runFromTrigger(TriggerBlock trigger, WorkflowRequest request) {
        
        long start = System.currentTimeMillis();
        
        ExecutionResult result = new ExecutionResult(request.getLogicContext());

        try {

            CustomNode node = trigger.getNode();
            
            Workflow workflow = node.getWorkflow();
            
            request.pushPackage(workflow.getPackage());

            workflow.executeWorkflow(node, request);
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

        Logger.debug(WorkflowEngine.class, "Flow execution time: {} ms.", System.currentTimeMillis() - start);
        return result;
    }
    
    /**
     * Extract flow package and start node name.
     * @param flow
     * @return
     * @throws FlowExecutionException
     */
    public static String[] parseFlowName(String flow) throws FlowExecutionException {

        if (flow == null)
            throw new FlowExecutionException("Flow is undefined.");
        
        String[] array = flow.split("-");
        
        if (array.length != 2)
        {
            throw new FlowExecutionException("Incorrect flow name. Must be package.name.FlowName-StartNode");
        }
        
        array[0] =  array[0].replace('.', '/');
                
        return array;
    }

    /**
     * Get flow by name. Should be without .n4j extension. E.g.
     * package.name.FlowName
     * 
     * @param flowName
     * @return
     */
    private static Workflow loadFlow(String flowName) {
        try {
            return WorkflowMngImpl.getInstance().lookupWorkflow(flowName);
        } catch (FlowInitializationException e) {
            Logger.error(WorkflowEngine.class, e.getMessage(), e);
        }
        return null;
    }
    
    public static void setCustomBlockInitStrategy(CustomBlockInitStrategy newStrategy)
    {
        CustomBlockLoader.getInstance().setInitStrategy(newStrategy);
    }

}
