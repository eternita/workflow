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

package org.neuro4j.workflow.enums;

/**
 *  Flow can be public or private.
 *  Public flow can be called by from outside.
 *  Private flow can be called just by other flow.
 */
public enum FlowVisibility {
    Public,
    Private;

    private static String[] opNames = new String[] { Public.name(), Private.name() };

    public static FlowVisibility getDefault()
    {
        return Public;
    }

    public static String[] types() {
        return opNames;
    }
    
    public static FlowVisibility getByName(String name)
    {
        for (FlowVisibility d : values())
        {
            if (d.name().equalsIgnoreCase(name))
            {
                return d;
            }
        }
        return null;
    }
    
    public static FlowVisibility getDefaultType()
    {
        return Public;
    }
}
