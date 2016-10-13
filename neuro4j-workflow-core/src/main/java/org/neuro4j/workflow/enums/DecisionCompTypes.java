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
 * Comparison type can be :
 * - constant value - ex. 10 or false
 * - value from context - processor will get object from flow context.
 * 
 */
public enum DecisionCompTypes {

    constant(0, "constant value"), context(1, "value from context");

    private static String[] opNames = new String[] { constant.getDisplayName(), context.getDisplayName() };

    public int value;
    private String display;

    /**
     * Constructor
     * 
     * @param value
     *        the value
     * @param display
     *        the name
     */
    private DecisionCompTypes(int value, String display) {
        this.value = value;
        this.display = display;
    }

    /**
     * Returns dispalyName.
     * 
     * @return the display name.
     */
    public String getDisplayName()
    {
        return display;
    }

    /**
     * Returns operators.
     * 
     * @return operators
     */
    public static String[] operators() {
        return opNames;
    }

    /**
     * Returns operator type by name.
     * 
     * @param name
     *        the name of operator.
     * @return operator type
     */
    public static DecisionCompTypes getByName(String name)
    {
        for (DecisionCompTypes d : values())
        {
            if (d.name().equals(name))
            {
                return d;
            }
        }
        return null;
    }
};
