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
package org.neuro4j.workflow.common;

import java.util.HashMap;
import java.util.Optional;

import org.neuro4j.workflow.enums.FlowVisibility;
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.node.WorkflowNode;

/**
 * Representation of single workflow unit. Holds information about all nodes in current workflow
 */
public class Workflow {


    /**
     * Holds information about all Start nodes
     */
    private final HashMap<String, StartNode> startNodes = new HashMap<String, StartNode>();
   
    /**
     *  Keeps all nodes
     */
    private final HashMap<String, WorkflowNode> nodes = new HashMap<String, WorkflowNode>(); 

    /**
     * FlowVisibility: can be Public or Private
     */
    private FlowVisibility visibility = FlowVisibility.getDefault();

    /**
     *  Flow's paclage (ex. org.mydomain)
     */
    private final String flowPackage;

    /**
     *  Flow name (ex. org.mydomain.MyFlow)
     */
    private final String flowName;

    public Workflow(String flowName, String flowPackage) {
        super();
        this.flowName = flowName;
        this.flowPackage = flowPackage;
    }


    /**
     * Returns package.
     * @return package name
     */
    public String getPackage() {
        return flowPackage;
    }

    public StartNode getStartNode(String name) {
        return startNodes.get(name);

    }


    public boolean isPublic() {
        return visibility == FlowVisibility.Public;
    }
    

    public WorkflowNode getById(String uuid) {
        return nodes.get(uuid);
    }

    public void registerNode(WorkflowNode entity) {
        nodes.put(entity.getUuid(), entity);
    }

    public void registerStartNode(StartNode entity) {
        startNodes.put(entity.getName(), entity);
    }

    public void setVisibility(final FlowVisibility visibility) {
        this.visibility = Optional.ofNullable(visibility).orElse(FlowVisibility.getDefault());
    }

    /**
     * Returns flow name
     * @return flow name
     */
    public String getFlowName() {
        return flowName;
    }


}
