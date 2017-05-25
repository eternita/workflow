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
package org.neuro4j.workflow.loader.f4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "node")
public class NodeXML {

    //public FlowXML flow;

    @XmlAttribute(required = true)
    public String uuid;

    @XmlAttribute(required = true)
    public String name;

    @XmlAttribute(required = true)
    public String type;

    @XmlElement(name = "description", required = false)
    public String description;

    @XmlElementWrapper(name = "parameters", required = false)
    @XmlElement(name = "parameter")
    public List<ParameterXML> parameters = new ArrayList<ParameterXML>();

    @XmlElementWrapper(name = "config", required = false)
    @XmlElement(name = "parameter")
    public List<ParameterXML> config = new ArrayList<ParameterXML>();

    @XmlElementWrapper(name = "transitions", required = false)
    @XmlElement(name = "transition")
    public List<TransitionXML> transitions = new ArrayList<TransitionXML>();

    public NodeXML() {
        super();
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }


    public List<TransitionXML> getRelations() {
        return transitions;
    }


    public String getParameter(String key) {
        for (ParameterXML param : parameters) {
            if (param.key.equals(key)) {
                return param.value;
            }
        }
        return null;
    }

    public String getConfig(String key) {
        for (ParameterXML param : config) {
            if (param.key.equals(key)) {
                return param.value;
            }
        }
        return null;
    }

}
