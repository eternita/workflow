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
package org.neuro4j.web.render.velocity;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.SingleThreadModel;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.tools.view.VelocityViewServlet;
import org.neuro4j.web.logic.render.ViewNodeRenderEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderExecutionException;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.log.Logger;

public class VelocityViewNodeRenderEngine implements ViewNodeRenderEngine {

    private Class<?> servletClass = org.apache.velocity.tools.view.VelocityViewServlet.class;
    private VelocityViewServlet theServlet;
    protected static final String DEFAULT_TEMPLATE_PATH = "/WEB-INF/velotemplates";
    protected static final String DEFAULT_TEMPLATE_PATH_KEY = "webapp.resource.loader.path";
    protected String TEMPLATE_PATH = DEFAULT_TEMPLATE_PATH;

    @Override
    public void init(ServletConfig config, ServletContext servletContext)
            throws ViewNodeRenderExecutionException {

        if (theServlet == null)
        {
            try {
                theServlet = (VelocityViewServlet) this.servletClass.newInstance();
                theServlet.init(config);
                loadRelativePath(config);

            } catch (Exception e) {
                Logger.error(this,e);
                throw new ViewNodeRenderExecutionException(e.getMessage());
            }
        }

    }

    @Override
    public void render(HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext,
            FlowContext logicContext, String viewTemplate)
            throws ViewNodeRenderExecutionException {

        
        try
        {

            request.setAttribute("javax.servlet.include.servlet_path", viewTemplate);

            if ((this.theServlet instanceof SingleThreadModel))
            {
                synchronized (this) {
                    this.theServlet.service(request, response);
                }
            }
            else
                this.theServlet.service(request, response);
        } catch (UnavailableException ex)
        {
            String includeRequestUri = (String) request.getAttribute("javax.servlet.include.request_uri");

            if (includeRequestUri != null)
            {
                throw new ViewNodeRenderExecutionException(ex.getMessage());
            }
            int unavailableSeconds = ex.getUnavailableSeconds();
            if (unavailableSeconds <= 0) {
                unavailableSeconds = 60;
            }
            // this.available = (System.currentTimeMillis() + unavailableSeconds * 1000L);

            try {
                response.sendError(503, ex.getMessage());
            } catch (IOException e) {
                throw new ViewNodeRenderExecutionException(e.getMessage());
            }
        } catch (Exception ex) {

            throw new ViewNodeRenderExecutionException(ex.getMessage());
        }
    }

    private void loadRelativePath(ServletConfig config)
    {
        String path = findInitParameter(config, DEFAULT_TEMPLATE_PATH_KEY);
        if (path != null)
        {
            TEMPLATE_PATH = path;
        }

    }

    protected String findInitParameter(ServletConfig config, String key)
    {
        // check the servlet config
        String param = config.getInitParameter(key);

        if (param == null || param.length() == 0)
        {
            // check the servlet context
            ServletContext servletContext = config.getServletContext();
            param = servletContext.getInitParameter(key);
        }
        return param;
    }


}
