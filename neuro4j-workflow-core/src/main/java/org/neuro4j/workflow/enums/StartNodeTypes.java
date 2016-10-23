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
package org.neuro4j.workflow.enums;

public enum StartNodeTypes {

    PUBLIC(0, "Public"), PRIVATE(1, "Private");

    private static String[] opNames = new String[] { PUBLIC.getDisplayName(), PRIVATE.getDisplayName() };

    public int value;
    private String display;

    private StartNodeTypes(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getDisplayName()
    {
        return display;
    }

    public static String[] types() {
        return opNames;
    }

    public static StartNodeTypes getByDisplayName(String displayName)
    {
        for (StartNodeTypes d : values())
        {
            if (d.display.equals(displayName))
            {
                return d;
            }
        }
        return null;
    }

    public static StartNodeTypes getByName(String name)
    {
        for (StartNodeTypes d : values())
        {
            if (d.name().equals(name))
            {
                return d;
            }
        }
        return null;
    }

    public static StartNodeTypes getDefaultType()
    {
        return PUBLIC;
    }
};
