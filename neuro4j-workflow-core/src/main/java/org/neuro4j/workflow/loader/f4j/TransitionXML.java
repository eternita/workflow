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

package org.neuro4j.workflow.loader.f4j;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.WorkflowNode;

@XmlRootElement(name = "transition")
public class TransitionXML {

	@XmlAttribute
	String uuid;

	@XmlAttribute
	String name;

	@XmlAttribute
	String toNode;
	

	String fromNode;


	public TransitionXML() {
		super();
	}

	public TransitionXML(String ruuid) {
		super();
		this.uuid = ruuid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setToNode(String toNode) {
		this.toNode = toNode;
	}

	public String uuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String name() {
		return name;
	}

	public String toNode() {
		return toNode;
	}
	
	

	public String fromNode() {
		return fromNode;
	}

	public void setFromNode(String fromNode) {
		this.fromNode = fromNode;
	}

	public boolean isValid() {
		if (fromNode != null && toNode != null) {
			return true;
		}
		return false;
	}

    /**
     * @param workflow 
     * @param node
     */
    public Transition createTransition(Workflow workflow, WorkflowNode node) {
        Transition transition = new Transition(workflow);
        transition.setUuid(this.uuid);
        transition.setFromNode(node);
        transition.setName(this.name);
        return transition;
        
    }


	

}
