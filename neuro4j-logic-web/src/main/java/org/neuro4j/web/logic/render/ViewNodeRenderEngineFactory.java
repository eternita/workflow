/*
 * Copyright (c) 2013-2014, Neuro4j
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

package org.neuro4j.web.logic.render;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Instantiate ViewNodeRenderEngine by class name
 * 
 */
public class ViewNodeRenderEngineFactory {

    private final static Logger logger = Logger.getLogger(ViewNodeRenderEngineFactory.class.getName());

    private static Map<String, ViewNodeRenderEngine> renders = new HashMap<String, ViewNodeRenderEngine>();

    public static ViewNodeRenderEngine getViewNodeRenderEngine(ServletConfig config, ServletContext servletContext, String type) throws ViewNodeRenderEngineNotFoundException, ViewNodeRenderExecutionException
    {
        if (renders.containsKey(type))
        {
            return renders.get(type);
        }

        String renderClass = getRenderImplByType(type);
        try {

            Class<?> clazz = Class.forName(renderClass);
            Object fObj = clazz.newInstance();

            if (fObj instanceof ViewNodeRenderEngine)
            {
                ViewNodeRenderEngine renderEngine = (ViewNodeRenderEngine) fObj;

                renderEngine.init(config, servletContext);

                renders.put(type, (ViewNodeRenderEngine) fObj);

                return renders.get(type);
            }
        } catch (ClassNotFoundException e) {
            logger.severe("Can't create ViewNodeRenderEngine " + renderClass + " " + e);
        } catch (InstantiationException e) {
            logger.severe("Can't create ViewNodeRenderEngine " + renderClass + " " + e);
        } catch (IllegalAccessException e) {
            logger.severe("Can't create ViewNodeRenderEngine " + renderClass + " " + e);
        }
        throw new ViewNodeRenderEngineNotFoundException("ViewNodeRenderEngine " + renderClass + " not found");
    }

    private static String getRenderImplByType(String type)
    {
        String renderClass = "org.neuro4j.web.logic.render.JspViewNodeRenderEngine";
        if ("jasper".equals(type))
        {
            renderClass = "org.neuro4j.web.render.jasper.JasperViewNodeRenderEngine";
        }
        if ("velocity".equals(type))
        {
            renderClass = "org.neuro4j.web.render.velocity.VelocityViewNodeRenderEngine";
        }
        if ("richfaces".equals(type))
        {
            renderClass = "org.neuro4j.web.render.richfaces.RichfacesViewNodeRenderEngine";
        }
        if ("myfaces".equals(type))
        {
            renderClass = "org.neuro4j.web.render.myfaces.MyfacesViewNodeRenderEngine";
        }
        return renderClass;
    }

}
