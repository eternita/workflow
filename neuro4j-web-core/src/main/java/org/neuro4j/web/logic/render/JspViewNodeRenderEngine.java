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
package org.neuro4j.web.logic.render;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.web.logic.WebFlowConstants;
import org.neuro4j.workflow.FlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This render will process jsp pages if ViewNode has renderType = "jsp"
 * 
 */
public class JspViewNodeRenderEngine implements ViewNodeRenderEngine {

	private static final Logger Logger = LoggerFactory.getLogger(JspViewNodeRenderEngine.class);

	
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, FlowContext logicContext, String view) throws ViewNodeRenderExecutionException {
        
        // handle if view is not specified -> go to default page with trace
        if (null == view) {
            view = WebFlowConstants.DEFAULT_VIEW_PAGE;
        } else if (view.startsWith("/")) {
            view = WebFlowConstants.DEFAULT_VIEW_DIRECTORY + view.replaceFirst("/", "");
        } else {
            view = WebFlowConstants.DEFAULT_VIEW_DIRECTORY + view;
        }
        
        try {
            servletContext.getRequestDispatcher(view).forward(request, response);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            throw new ViewNodeRenderExecutionException(e);
        }
    }

    @Override
    public void init(ServletConfig config, ServletContext servletContext) throws ViewNodeRenderExecutionException {

    }

}
