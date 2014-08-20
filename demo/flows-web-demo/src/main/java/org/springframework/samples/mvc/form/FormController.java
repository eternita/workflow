package org.springframework.samples.mvc.form;

import javax.validation.Valid;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.springmvc.AbstractWorkflowController;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/form")
@SessionAttributes("formBean")
public class FormController extends AbstractWorkflowController{

	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(@Valid FormBean formBean, BindingResult result, 
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, 
								Model model, RedirectAttributes redirectAttrs) throws FlowExecutionException {
		if (result.hasErrors()) {
			return null;
		}
		
		model.addAttribute("formBean", formBean);
		FlowContext flowContext = processWorkflow("org.springframework.samples.mvc.form.SimpleFlow-Start", model, null, null);

		String message = (String) flowContext.get("message");
		if (ajaxRequest) {
			model.addAttribute("message", message);
			return "views/form";
		} else {
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/form";			
		}
	}
	
	// Invoked on every request

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}


	@ModelAttribute("formBean")
	public FormBean createFormBean() {
		return new FormBean();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView form() {
		return new ModelAndView("views/form");
	}
	
}
