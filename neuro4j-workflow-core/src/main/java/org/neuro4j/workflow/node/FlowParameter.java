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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flowName == null) ? 0 : flowName.hashCode());
		result = prime * result + ((flowPackage == null) ? 0 : flowPackage.hashCode());
		result = prime * result + ((startNode == null) ? 0 : startNode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowParameter other = (FlowParameter) obj;
		if (flowName == null) {
			if (other.flowName != null)
				return false;
		} else if (!flowName.equals(other.flowName))
			return false;
		if (flowPackage == null) {
			if (other.flowPackage != null)
				return false;
		} else if (!flowPackage.equals(other.flowPackage))
			return false;
		if (startNode == null) {
			if (other.startNode != null)
				return false;
		} else if (!startNode.equals(other.startNode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlowParameter [flowName=" + flowName + ", flowPackage=" + flowPackage + ", startNode=" + startNode
				+ "]";
	}
	

}
