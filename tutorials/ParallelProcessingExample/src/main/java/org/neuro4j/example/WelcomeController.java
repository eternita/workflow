package org.neuro4j.example;

import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

	@Autowired
	private WorkflowEngine engine;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}

	@RequestMapping("/download")
	public String download(Map<String, Object> model) {

		WorkflowRequest request = new WorkflowRequest(model);
		
		request.addParameter("url1", "https://en.m.wikipedia.org/wiki/Antarctica/Rothera#/random");
		request.addParameter("url2", "https://en.m.wikipedia.org/wiki/Bengal_tiger");
		request.addParameter("url3", "https://en.m.wikipedia.org/wiki/Bengal");
		
		ExecutionResult result = engine.execute("org.neuro4j.workflow.DownloadPages-Start", request);
		model.putAll(result.getFlowContext().getParameters());
		return "download";
	}
	
}
