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
package org.neuro4j.workflow;

import static org.neuro4j.workflow.loader.f4j.SWFConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keeps all in/out variables during flow execution. 
 *
 */
public class FlowContext {

	private static final Logger logger = LoggerFactory.getLogger(FlowContext.class);

    /**
     * 
     */
    private Map<String, Object> parameters = new HashMap<String, Object>();
    /**
     * Name of template which should be rendered after flow execution.
     * Can be used in web application.
     */
    private String viewTemplate;
    
    /**
     *  For web application holds render type. Ex. jsp, jasper. velocity
     */
    private String renderType;
    
    /**
     * Holds request locale.
     */
    private Locale locale;

    public FlowContext() {
        super();
        locale = Locale.US;
    }

    /**
     * @param map of request parameters
     */
    public FlowContext(Map<String, Object> map) {
        parameters.putAll(map);
    }

    public void put(String key, Object value) {
        parameters.put(key, value);
    }

    public Object get(String key)
    {
        if (key == null)
        {
            return null;
        }
        key = key.trim();

        if (key.startsWith(QUOTES_SYMBOL) && key.endsWith(QUOTES_SYMBOL) && key.length() > 1)
        {
            return key.substring(1, key.length() - 1);
        }
        if (key.contains("."))
        {
            int pointIndex = key.indexOf(".");
            String firstObj = key.substring(0, pointIndex);
            Object obj = parameters.get(firstObj);
            if (obj != null)
            {
                String utilKey = key.substring(pointIndex + 1);
                try {
                    obj = PropertyUtils.getProperty(obj, utilKey);
                    return obj;
                } catch (Exception e) {
                	logger.error(e.getMessage(), e);
                }
            } else {
                return null;
            }
        }

        return parameters.get(key);
    }

    public Set<String> keySet()
    {
        return parameters.keySet();
    }

    public Object remove(String key)
    {
        return parameters.remove(key);
    }

    public String getViewTemplate() {

        return viewTemplate;
    }

    public void setViewTemplate(String viewTemplate) {
        this.viewTemplate = viewTemplate;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        // sort asc
        List<String> ks = new ArrayList<String>(parameters.keySet());
        Collections.sort(ks);
        Iterator<String> localIterator = ks.iterator();
        if (!(localIterator.hasNext())) {
            return "{}";
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append('{');
        while (true) {
            Object localObject1 = localIterator.next();
            Object localObject2 = parameters.get(localObject1);
            localStringBuilder.append((localObject1 == this) ? "(this Map)" : localObject1);
            localStringBuilder.append('=');
            localStringBuilder.append((localObject2 == this) ? "(this Map)" : localObject2);
            if (!(localIterator.hasNext())) {
                localStringBuilder.append('}');
                break;
            }
            localStringBuilder.append(", ");
        }
        return localStringBuilder.toString();
    }
    
    public Map<String, Object> getParameters(){        
        return Collections.unmodifiableMap(parameters);
    }

}
