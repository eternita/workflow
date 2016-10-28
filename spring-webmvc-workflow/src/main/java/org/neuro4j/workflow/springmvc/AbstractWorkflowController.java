/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.neuro4j.workflow.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.springframework.context.SpringContextInitStrategy;
import org.neuro4j.web.workflow.core.WebRequest;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * Helps to call flows from spring-mvc controllers.
 * 
 */
public class AbstractWorkflowController{
	
    @Bean
    WorkflowEngine getWorkflowEngine(ConfigurableListableBeanFactory beanFactory){
    	WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withCustomBlockInitStrategy(new SpringContextInitStrategy(beanFactory)));
    	return engine;
    }
    
    @Autowired
    WorkflowEngine engine;

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
        ExecutionResult result = engine.execute(flow, request);
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
        ExecutionResult result = engine.execute(flow, request);
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
    
    
    
}
