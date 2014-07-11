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

package org.neuro4j.workflow.log;

import org.slf4j.LoggerFactory;

public class Logger {

    public static final String PERFORMANCE_FLOWS = "PERFORMANCE_FLOWS";

    public static void error(Object category, String msg, Object[] params)
    {
        if (params.length == 0)
        {
            getLogger(category).error(msg);
        }
        else
        {
            getLogger(category).error(msg, params);
        }
    }

    public static void error(Object category, String msg)
    {
        getLogger(category).error(msg);
    }

    public static void error(Object category, String msg, Object param)
    {
        getLogger(category).error(msg, param);
    }

    public static void error(Object category, String msg, Object param1, Object param2)
    {
        getLogger(category).error(msg, param1, param2);
    }

    public static void error(Object category, String msg, Throwable ex)
    {
        getLogger(category).error(msg, ex);
    }

    public static void info(Object category, String msg, Throwable ex)
    {
        getLogger(category).info(msg, ex);
    }

    public static void info(Object category, String msg, Object[] params)
    {
        if (params.length == 0)
        {
            getLogger(category).info(msg);
        }
        else
        {
            getLogger(category).info(msg, params);
        }
    }

    public static void info(Object category, String msg, Object param1)
    {
        getLogger(category).info(msg, param1);
    }

    public static void info(Object category, String msg, Object param1, Object param2)
    {
        getLogger(category).info(msg, param1, param2);
    }

    public static void debug(Object category, String msg, Object[] params)
    {
        if (params.length == 0)
        {
            getLogger(category).debug(msg);
        }
        else
        {
            getLogger(category).debug(msg, params);
        }
    }

    public static void debug(Object category, String msg, Object param1)
    {
        getLogger(category).debug(msg, param1);
    }

    public static void debug(Object category, String msg, Object param1, Object param2)
    {
        getLogger(category).debug(msg, param1, param2);
    }

    public static void debug(Object category, String msg, Throwable ex)
    {
        getLogger(category).debug(msg, ex);
    }

    public static void warn(Object category, String msg, Object[] params)
    {
        if (params.length == 0)
        {
            getLogger(category).warn(msg);
        }
        else
        {
            getLogger(category).warn(msg, params);
        }
    }

    public static void warn(Object category, String msg, Throwable ex)
    {
        getLogger(category).warn(msg, ex);
    }

    private static org.slf4j.Logger getLogger(Object category)
    {
        if ((category instanceof Class))
        {
            return LoggerFactory.getLogger((Class) category);
        }
        if ((category instanceof String))
        {
            return LoggerFactory.getLogger((String) category);
        }
        if (category != null)
        {
            return LoggerFactory.getLogger(category.getClass());
        }

        return LoggerFactory.getLogger("undefined");
    }

}
