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

package org.neuro4j.workflow.enums;

public enum DecisionOperators {

    EQ_STR(0, "= (string)", false), NEQ_STR(1, "!= (string)", false), DEFINED(2, "defined", true), UNDEFINED(3, "undefined", true), EMPTY_STR(4, "empty string", true), EQ(5, "==", false), NEQ(6, "!=", false), HAS_EL(7, "has elements", true), LESS(8, "<", false), GREATER(9, ">", false);

    private static String[] opNames = new String[] { EQ_STR.getDisplayName(), NEQ_STR.getDisplayName(), DEFINED.getDisplayName(), UNDEFINED.getDisplayName(), EMPTY_STR.getDisplayName(), EQ.getDisplayName(), NEQ.getDisplayName(), HAS_EL.getDisplayName(), LESS.getDisplayName(), GREATER.getDisplayName() };

    public int value;
    private String display;
    private boolean singleOperand = false;

    private DecisionOperators(int value, String display, boolean singleOperand) {
        this.value = value;
        this.display = display;
        this.singleOperand = singleOperand;
    }

    public String getDisplayName()
    {
        return display;
    }

    public static String[] operators() {
        return opNames;
    }

    public static DecisionOperators getByName(String name)
    {
        for (DecisionOperators d : values())
        {
            if (d.name().equals(name))
            {
                return d;
            }
        }
        return null;
    }

    public boolean isSingleOperand() {
        return singleOperand;
    }

};
