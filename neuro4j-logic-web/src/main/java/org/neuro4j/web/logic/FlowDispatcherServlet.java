/*
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuro4j.web.logic;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.web.logic.render.ViewNodeRenderEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderEngineFactory;
import org.neuro4j.web.logic.render.ViewNodeRenderExecutionException;
import org.neuro4j.web.workflow.core.WebRequest;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.log.Logger;

public class FlowDispatcherServlet extends HttpServlet {

	WorkflowEngine engine;
	
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;


    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        String urlStr = request.getRequestURI(); // /not-root-context/n4j/en_US/Welcome-start /n4j/en_US/Welcome-start
        String[] urlSplit = urlStr.split("/");
        String flow = urlSplit[urlSplit.length - 1]; // the last part

        WebRequest webRequest = new WebRequest(request, response);

        String view = null;

        { // get locale
            String localeString = null;
            // check if locale defined in URL
            // locale can be right before flow (if any)
            if (urlSplit.length > 2)
                localeString = urlSplit[urlSplit.length - 2];

            Locale locale = getLocale(localeString);
            if (null == locale)
                locale = Locale.getDefault();

            Logger.debug(this, "Using '{}' locale", locale);

            webRequest.setRequestLocale(locale);
        }

        for (Object param : request.getParameterMap().keySet())
            webRequest.addParameter((String) param, request.getParameter((String) param));

        ExecutionResult result = engine.execute(flow, webRequest);

        if (result.getException() == null)
        {
            FlowContext context = result.getFlowContext();

            // copy data from context to request attributes (for usage in views)
            for (String key : context.keySet())
            {
                request.setAttribute(key, context.get(key));
            }

            view = (String) context.getViewTemplate();

            String renderType = (String) context.getRenderType();

            Logger.debug(this, "Using '{}' web render", renderType);

            if (null != renderType && renderType.trim().length() > 0)
            {
                try {
                    ViewNodeRenderEngine renderEngine = ViewNodeRenderEngineFactory.getViewNodeRenderEngine(getServletConfig(), getServletContext(), renderType);
                    renderEngine.render(request, response, getServletContext(), context, view);
                } catch (ViewNodeRenderExecutionException e) {
                    processFlowExeption(request, response, e);
                }
            }

        } else {
            processFlowExeption(request, response, result.getException());
        }

        return;
    }

    /**
     * Processes errors. Redirects to error page.
     * 
     * @param request
     * @param response
     * @param exeption
     * @throws ServletException
     * @throws IOException
     */
    private void processFlowExeption(HttpServletRequest request, HttpServletResponse response, Exception exeption) throws ServletException, IOException {
        request.setAttribute("exception", exeption);
        Logger.error(this, exeption);
        getServletContext().getRequestDispatcher(WebFlowConstants.ERROR_PAGE).forward(request, response);
    }

    /**
     * Returns request's locale.
     * 
     * @param localeString
     * @return
     */
    private Locale getLocale(String localeString)
    {
        if (null == localeString)
            return Locale.getDefault();

        // Extract language
        int languageIndex = localeString.indexOf('_');
        String language = null;
        if (languageIndex == -1)
        {
            // No further "_" so is "{language}" only
            return new Locale(localeString, "");
        }
        else
        {
            language = localeString.substring(0, languageIndex);
        }

        // Extract country
        int countryIndex = localeString.indexOf('_', languageIndex + 1);
        String country = null;
        if (countryIndex == -1)
        {
            // No further "_" so is "{language}_{country}"
            country = localeString.substring(languageIndex + 1);
            return new Locale(language, country);
        }
        else
        {
            // Assume all remaining is the variant so is "{language}_{country}_{variant}"
            country = localeString.substring(languageIndex + 1, countryIndex);
            String variant = localeString.substring(countryIndex + 1);
            return new Locale(language, country, variant);
        }

    }

}
