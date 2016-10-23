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
package org.neuro4j.web.render.json;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.web.logic.render.ViewNodeRenderEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderExecutionException;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.log.Logger;

/**
 * This class processes response in json format.
 *
 */
public class JsonViewNodeRenderEngine implements ViewNodeRenderEngine {

    public static final String JSON_MIME_TYPE = "application/json";

    
    @Override
    public void init(ServletConfig config, ServletContext servletContext) throws ViewNodeRenderExecutionException {

    }

    
    /**
     * 
     */
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, FlowContext logicContext, String view) throws ViewNodeRenderExecutionException
    {

        if (view == null)
        {
            throw new ViewNodeRenderExecutionException("JsonViewNodeRenderEngine: View is null");
        }
        response.setContentType(JSON_MIME_TYPE);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(view);
            writer.flush();
        } catch (IOException e) {
            Logger.error(this, e);
            throw new ViewNodeRenderExecutionException(e);
        }

        return;
    }
}
