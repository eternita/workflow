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

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instantiate ViewNodeRenderEngine by render type.
 * 
 */
public class ViewNodeRenderEngineFactory {

	private static final Logger Logger = LoggerFactory.getLogger(JspViewNodeRenderEngine.class);

	
    private static Hashtable<String, ViewNodeRenderEngine> renders = new Hashtable<String, ViewNodeRenderEngine>();

    public static ViewNodeRenderEngine getViewNodeRenderEngine(ServletConfig config, ServletContext servletContext, String renderType) throws ViewNodeRenderExecutionException
    {
        
        ViewNodeRenderEngine engine = null;

        if (renders.containsKey(renderType))
        {
            engine = renders.get(renderType);
        } else {

        synchronized (renders) {
            String renderClass = getRenderImpl(renderType);
            try {

                Class<?> clazz = Class.forName(renderClass);
                Object fObj = clazz.newInstance();

                if (fObj instanceof ViewNodeRenderEngine)
                {
                    engine = (ViewNodeRenderEngine) fObj;

                    engine.init(config, servletContext);

                    renders.put(renderType, engine);

                }
            } catch (Exception e) {
                Logger.error(e.getMessage(), e);
                throw new ViewNodeRenderExecutionException("ViewNodeRenderEngine " + renderClass + " not found");
            }
            
        }
        }
        return engine;
    }

    /**
     * Returns class which will process vew template
     * @param renderType
     * @return
     * @throws ViewNodeRenderExecutionException
     */
    private static String getRenderImpl(String renderType) throws ViewNodeRenderExecutionException
    {
        String renderImpl = null;

        String fileName =   renderType + ".properties";

        InputStream input = null;
        Properties prop = new Properties();

        try {

            input = ViewNodeRenderEngineFactory.class.getClassLoader().getResourceAsStream(fileName);
            if (input == null)

                throw new ViewNodeRenderExecutionException("Implementation not found for renderType: " + renderType);

            prop.load(input);

            renderImpl = prop.getProperty("viewRender");

        } catch (IOException ex) {
            Logger.error(ex.getMessage(), ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Logger.error(e.getMessage(), e);
                }
            }
        }

        return renderImpl;
    }

}
