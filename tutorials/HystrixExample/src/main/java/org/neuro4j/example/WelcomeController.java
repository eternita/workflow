package org.neuro4j.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class WelcomeController {
	
	private static final Logger Logger = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	private WorkflowEngine engine;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}

	@RequestMapping("/download")
	public RedirectView download(Map<String, Object> model) {


		
		List<String> urls = new ArrayList<>();
		
		for (int i =0; i < 20; i++){
			urls.add("https://en.m.wikipedia.org/wiki/Antarctica/Rothera#/random");
			urls.add("https://en.m.wikipedia.org/wiki/Bengal_tiger");
			urls.add("https://en.m.wikipedia.org/wiki/Bengal");
			urls.add("https://en.wikipedia.org/wiki/List_of_Swedish_monarchs");
			urls.add("https://en.wikipedia.org/wiki/Monarchy_of_Sweden");
			
		}
		
		model.put("urls", urls);
		
		
		try {
			engine.executeAsync("org.neuro4j.workflow.DownloadPages-Start", model);
		} catch (FlowExecutionException e) {
			Logger.error(e.getMessage(), e);
		}

		return new RedirectView("/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2Fmetrics%2Fhystrix.stream");
	}
	
}
