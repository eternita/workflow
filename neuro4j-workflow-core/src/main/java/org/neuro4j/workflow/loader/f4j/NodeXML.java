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

package org.neuro4j.workflow.loader.f4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "node")
public class NodeXML {

    FlowXML flow;

    @XmlAttribute(required = true)
    String uuid;

    @XmlAttribute(required = true)
    String name;

    @XmlAttribute(required = true)
    Integer x;

    @XmlAttribute(required = true)
    Integer y;

    @XmlAttribute(required = true)
    String type;

    @XmlElement(name = "description", required = false)
    public String description;

    @XmlElementWrapper(name = "parameters", required = false)
    @XmlElement(name = "parameter")
    List<ParameterXML> parameters = new ArrayList<ParameterXML>();

    @XmlElementWrapper(name = "config", required = false)
    @XmlElement(name = "parameter")
    List<ParameterXML> config = new ArrayList<ParameterXML>();

    @XmlElementWrapper(name = "transitions", required = false)
    @XmlElement(name = "transition")
    List<TransitionXML> transitions = new ArrayList<TransitionXML>();

    public NodeXML() {
        super();
    }

    public NodeXML(String uuid) {
        super();
        this.uuid = uuid;
    }

    public NodeXML(String uuid, String name) {
        super();
        this.uuid = uuid;
        this.name = name;
    }

    public NodeXML(String uuid, String name, FlowXML workflow) {
        this(uuid, name);
        this.flow = workflow;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public List<ParameterXML> getParameters() {
        return parameters;
    }

    public List<TransitionXML> getRelations() {
        return transitions;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setConfig(List<ParameterXML> config) {
        this.config = config;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void registerExit(TransitionXML transition) {
        transitions.add(transition);
    }

    public void addParameter(String key, String value, boolean isInput) {
        parameters.add(new ParameterXML(key, value, isInput));

    }

    public String getParameter(String key) {
        for (ParameterXML param : parameters) {
            if (param.getKey().equals(key)) {
                return param.value;
            }
        }
        return null;
    }

    public void addConfig(String key, String value) {
        config.add(new ParameterXML(key, value));

    }

    public Integer x() {
        return x;
    }

    public Integer y() {
        return y;
    }

    public String type() {
        return type;
    }

    public String getConfig(String key) {
        for (ParameterXML param : config) {
            if (param.getKey().equals(key)) {
                return param.value;
            }
        }
        return null;
    }

}
