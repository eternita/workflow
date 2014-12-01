package org.neuro4j.workflow.tutorial.restful;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.springmvc.AbstractWorkflowController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController extends AbstractWorkflowController {

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws FlowExecutionException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", name);
		FlowContext context =  processWorkflow("org.neuro4j.workflow.tutorial.mvc.views.ViewFlow-GetGreeting", new WorkflowRequest(parameters));
		Greeting greeting = (Greeting)context.get("message");
		return greeting;
	}

}
