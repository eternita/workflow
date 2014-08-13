package org.springframework.samples.mvc.simple;

import javax.servlet.http.HttpServletRequest;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.springmvc.AbstractWorkflowController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Controller
public class SimpleController extends AbstractWorkflowController implements BeanFactoryAware{

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

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        UrlBasedViewResolver resolver =  beanFactory.getBean(org.springframework.web.servlet.view.UrlBasedViewResolver.class);
    
        System.out.println("Print");
    }
	
}
