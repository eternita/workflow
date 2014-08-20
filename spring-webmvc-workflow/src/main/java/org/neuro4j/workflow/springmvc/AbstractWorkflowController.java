package org.neuro4j.workflow.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.web.workflow.core.WebRequest;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * Helps to call flows from spring-mvc controllers.
 * 
 */
public class AbstractWorkflowController implements BeanFactoryAware{

    private static final String JSP_SUFFIX = ".jsp";

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

    /**
     * 
     * @param flow
     * @param request
     * @return ModelAndView object if flow was ended with ViewNode
     * @throws FlowExecutionException
     */
    protected ModelAndView processWorkflowWithModelView(String flow, WorkflowRequest request) throws FlowExecutionException {
        ExecutionResult result = WorkflowEngine.run(flow, request);
        if (result.getException() != null) {
           throw result.getException(); 
        }
        return createModelView(result.getFlowContext());
    }

    /**
     * Creates ModelAndView object and process working template from flowContext.
     * Working template contains full path from WEB-INF and has file ext ".jsp".
     * 
     * @param context
     * @return
     */
    protected ModelAndView createModelView(FlowContext context) throws FlowExecutionException
	{
		String viewTemplate = context.getViewTemplate();
		if (viewTemplate == null)
		{
			throw new FlowExecutionException("Working template is null. Please check your template for ViewNode");
		}
		if (viewTemplate.endsWith(JSP_SUFFIX))
		{
		    viewTemplate = viewTemplate.replace(JSP_SUFFIX, "");
		}
		
		//TODO: replace .jsp . and path in WEB-INF folder.		
        /**
         *  <beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
                <beans:property name="prefix" value="/WEB-INF/" />
                <beans:property name="suffix" value=".jsp" />
            </beans:bean>
         * 
         * 
         */
		ModelAndView view = new ModelAndView(viewTemplate);
		view.addAllObjects(context.getParameters());			
		return view;
	}
    

    
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        
    }
    
    
}
