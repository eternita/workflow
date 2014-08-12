package org.springframework.samples.mvc.exceptions;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExceptionController {

	@RequestMapping("/exception")
	public @ResponseBody String exception() throws FlowExecutionException {
		throw new FlowExecutionException("Sorry!");
	}


	@ExceptionHandler
	public @ResponseBody String handle(FlowExecutionException e) {
		return "FlowExecutionException handled!";
	}

}
