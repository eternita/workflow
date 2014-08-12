package org.springframework.samples.mvc.simple;

import javax.servlet.http.HttpServletRequest;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.springmvc.AbstractWorkflowController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleController extends AbstractWorkflowController{

	@RequestMapping("/simple")
	public @ResponseBody String simple(HttpServletRequest request) throws FlowExecutionException {		
		FlowContext context = processWorkflow("org.springframework.samples.mvc.simple.SimpleFlow-Start", new WorkflowRequest());
		String message = (String) context.get("helloMessage");
		return message;
	}
	
	@ExceptionHandler
	public @ResponseBody String handle(FlowExecutionException e) {
		return "FlowExecutionException handled!";
	}

}
