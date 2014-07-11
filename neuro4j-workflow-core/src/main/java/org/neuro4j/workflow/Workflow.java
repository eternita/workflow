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

package org.neuro4j.workflow;

import java.util.Collection;
import java.util.HashMap;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.enums.NetworkVisibility;
import org.neuro4j.workflow.loader.LogicBlockLoader;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.LogicBlock;
import org.neuro4j.workflow.xml.StartNode;
import org.neuro4j.workflow.xml.WorkflowNode;

public class Workflow {

    private long loadtime = -1;

    HashMap<String, StartNode> startNodes = null;
    HashMap<String, WorkflowNode> nodes = null;

    NetworkVisibility visibility = NetworkVisibility.Public;

    private String flowPackage;

    private String flowName;

    public Workflow(String flowName, String flowPackage) {
        this();
        this.flowName = flowName;
        this.flowPackage = flowPackage;
        this.loadtime = System.currentTimeMillis();
    }

    private Workflow() {

        startNodes = new HashMap<String, StartNode>();
        nodes = new HashMap<String, WorkflowNode>();
    }

    public String getPackage() {
        return flowPackage;
    }

    public StartNode getStartNode(String name) {
        return startNodes.get(name);

    }
    
    public Collection<StartNode> getStartNodes() {
        return startNodes.values();
    }
    

    public boolean isPublic() {
        return visibility == NetworkVisibility.Public;
    }

    public void executeWorkflow(StartNode startNode, WorkflowRequest request) throws FlowExecutionException {

        if (null == request)
            throw new RuntimeException("WorkflowRequest must not be null");

        WorkflowNode step = startNode;

        while (null != step)
        {
            try {
                LogicBlock logicNode = LogicBlockLoader.getInstance().lookupBlock(step);

                // Set<Connected> stack = getExecutionStack(logicContext);
                // stack.add(currentStep);
                //
                // logicContext.put(SWFConstants.AC_CURRENT_NODE, workflowNode);

                long startTime = System.currentTimeMillis();

                Logger.debug(Workflow.class, "		Running: {} ({})", logicNode.getClass().getSimpleName(), logicNode.getClass().getCanonicalName());

                WorkflowNode lastNode = step;

                step = logicNode.process(request);

                request.setLastSuccessfulNode(lastNode);

                Logger.debug(Workflow.class, "		Finished: ({}ms) {}", System.currentTimeMillis() - startTime, logicNode.getClass().getSimpleName());

            } catch (ExecutableEntityNotFoundException e1) {
                throw new FlowExecutionException(e1.getCause());
            } catch (FlowInitializationException e) {
                throw new FlowExecutionException(e.getCause());
            }

            if (step != null)
            {
                Logger.debug(Workflow.class, "Next step: {} ({})", step.getName(), step.getUuid());
            }

        }

    }

    public WorkflowNode getById(String uuid) {
        return nodes.get(uuid);
    }

    public Collection<WorkflowNode> getNodes() {
        return nodes.values();
    }

    public void registerNode(WorkflowNode entity) {
        nodes.put(entity.getUuid(), entity);
    }

    public void registerStartNode(StartNode entity) {
        startNodes.put(entity.getName(), entity);
    }

}
