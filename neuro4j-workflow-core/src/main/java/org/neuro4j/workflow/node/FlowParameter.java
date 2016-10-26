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
package org.neuro4j.workflow.node;

import static org.neuro4j.workflow.utils.Validation.*;

import org.neuro4j.workflow.common.FlowExecutionException;


/**
 * Represents flow request. Contains full flow's name, package and request's start node
 *
 */
public class FlowParameter {
	
	private String flowName;
	private String flowPackage;
	private String startNode;
	
	private FlowParameter(String name, String flowPackage, String startNode){
		this.flowName = name;
		this.flowPackage = flowPackage;
		this.startNode = startNode;
	}
	
	/**
	 * Parses request(ex. org.mydomain.MyFlow-MyStartNode)
	 * @param request parameter
	 * @return FlowParameter object
	 * @throws FlowExecutionException in case of wrong format
	 */
	public static FlowParameter parse(String request) throws FlowExecutionException {
		requireNonNull(request, () -> new FlowExecutionException("Request flow can not be null"));

		String[] array = request.split("-");

		if (array.length < 1) {
			throw new FlowExecutionException("Incorrect flow name. Must be package.name.FlowName-StartNode");
		}

		String flowPackage = "default";
		
		String  flow = array[0];
		
		String startNode = null;
		if (array.length == 2) {
			startNode = array[1];
		}
		int index = flow.lastIndexOf(".");
		
		if (index > 0) {
			flowPackage = flow.substring(0, index);
		}

		return new FlowParameter(flow, flowPackage, startNode);
	}

	public String getFlowName() {
		return flowName;
	}

	public String getFlowPackage() {
		return flowPackage;
	}

	public String getStartNode() {
		return startNode;
	}


	@Override
	public String toString() {
		return "FlowParameter [flowName=" + flowName + ", flowPackage=" + flowPackage + ", startNode=" + startNode
				+ "]";
	}
	

}
