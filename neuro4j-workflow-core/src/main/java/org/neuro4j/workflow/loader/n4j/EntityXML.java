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

package org.neuro4j.workflow.loader.n4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowNode;

public class EntityXML {

    @XmlAttribute
    String uuid;

    @XmlAttribute
    String name;

    @XmlJavaTypeAdapter(PropertyListXMLAdapter.class)
    @XmlElement(name = "properties")
    List<PropertyXML> properties = new ArrayList<PropertyXML>();

    @XmlJavaTypeAdapter(RelationTailListXMLAdapter.class)
    @XmlElement(name = "connected")
    List<RelationTailXML> relations = new ArrayList<RelationTailXML>();

    public EntityXML() {
        super();
        // TODO Auto-generated constructor stub
    }

    public EntityXML(String uuid) {
        super();
        this.uuid = uuid;
    }

    public EntityXML(String uuid, String name) {
        super();
        this.uuid = uuid;
        this.name = name;
    }

    public EntityXML(WorkflowNode entity) {
        super();
        this.uuid = entity.getUuid();
        this.name = entity.getName();

        for (String key : entity.getParameters().keySet())
            properties.add(new PropertyXML(key, entity.getParameter(key)));

        for (Transition rid : entity.getExits())
            relations.add(new RelationTailXML(rid.getUuid()));

    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public List<PropertyXML> getRepresentations() {
        return properties;
    }

    public List<RelationTailXML> getRelations() {
        return relations;
    }

}
