package org.springframework.samples.mvc.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.springmvc.AbstractWorkflowController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/views/*")
public class ViewsController extends AbstractWorkflowController{

	@RequestMapping(value="html", method=RequestMethod.GET)
	public ModelAndView prepare(Model model, HttpServletRequest request, HttpServletResponse response) throws FlowExecutionException {
		model.addAttribute("foo", "bar");
		model.addAttribute("fruit", "apple");
		
		FlowContext context =  processWorkflow("org.springframework.samples.mvc.views.ViewFlow-Start", model, request, response);

		return  createModelView(context);
	}
	

}
