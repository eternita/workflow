package org.neuro4j.workflow.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.web.workflow.core.WebRequest;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.springframework.ui.Model;

/**
 * Helps to call flows from spring-mvc controllers.
 * 
 */
public class AbstractWorkflowController {

	/**
	 * Executes flow and puts values from flowContext to model object.
	 * 
	 * @param flow 
	 * @param model
	 * @param request
	 * @param response
	 * @return FlowContext
	 * @throws FlowExecutionException
	 */
	protected FlowContext processWorkflow(String flow, Model model, HttpServletRequest request, HttpServletResponse response) throws FlowExecutionException {

		WorkflowRequest workflowRequest = new WebRequest(model.asMap(), request, response);

		FlowContext context = processWorkflow(flow, workflowRequest);

		model.addAllAttributes(context.getParameters());
		
		return context;
	}

	/**
	 * 
	 * @param flow
	 * @param request
	 * @return
	 * @throws FlowExecutionException
	 */
	protected FlowContext processWorkflow(String flow, WorkflowRequest request) throws FlowExecutionException {
		ExecutionResult result = WorkflowEngine.run(flow, request);
		if (result.getException() != null) {
            throw new FlowExecutionException(result.getException());
		}
		return result.getFlowContext();
	}
}
