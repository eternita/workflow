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

import java.util.Collection;
import java.util.HashMap;

import org.neuro4j.workflow.enums.FlowVisibility;
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.node.WorkflowNode;

/**
 *
 */
public class Workflow {


    private final HashMap<String, StartNode> startNodes;
    protected final HashMap<String, WorkflowNode> nodes;

    FlowVisibility visibility = FlowVisibility.Public;

    private String flowPackage;

    private String flowName;

    public Workflow(String flowName, String flowPackage) {
        this();
        this.flowName = flowName;
        this.flowPackage = flowPackage;
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
        return visibility == FlowVisibility.Public;
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

    public void setVisibility(FlowVisibility visibility) {
        if (visibility == null)
        {
            visibility = FlowVisibility.Public;
        }
        this.visibility = visibility;
    }

    public String getFlowName() {
        return flowName;
    }



}
