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

package org.neuro4j.workflow.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.common.FlowExecutionException;

public class WorkflowNode {

    protected Workflow workflow = null;
    private Map<String, String> parameters = null;
    private String uuid = null;
    private String name;
    private String executableClass = null;

    Map<String, Transition> exits = null;

    public WorkflowNode() {
        exits = new HashMap<String, Transition>(3);
        parameters = new HashMap<String, String>(4);
    }

    public WorkflowNode(String name, String uuid, Workflow workflow) {
        this();
        setUuid(uuid);
        setName(name);
        this.workflow = workflow;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public String getExecutableClass(){
        return executableClass;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public String getParameter(String key) {

        return this.parameters.get(key);
    }

    public void setExecutableClass(String executableClass) {
        this.executableClass = executableClass;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public Transition getExitByName(String relationName) {
        return exits.get(relationName);
    }

    private void setUuid(String uuid2) {
        this.uuid = uuid2;
    }

    private void setName(String name) {
        this.name = name;
    }

    public void registerExit(Transition con) {
        con.setFromNode(this);
        exits.put(con.getName(), con);

    }

    public Collection<Transition> getExits() {
        return exits.values();
    }

    public void registerNodeInWorkflow() {
        workflow.registerNode(this);
    }

}
