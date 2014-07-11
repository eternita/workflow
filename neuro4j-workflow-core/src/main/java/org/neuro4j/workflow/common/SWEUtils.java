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

package org.neuro4j.workflow.common;

import org.neuro4j.workflow.loader.n4j.SWFConstants;

public class SWEUtils {

    public static String[] getMappedParameters(String value) {
        String[] splitted = new String[2];

        if (value != null) {
            String[] s = value.trim().split(SWFConstants.PARAMETER_DELIMITER);
            if (s != null && s.length > 1) {
                splitted[0] = s[0];
                if (s[1].equals("null")) {
                    splitted[1] = s[0];
                } else {
                    splitted[1] = s[1];
                }

            } else if (s != null && s.length == 1) {
                splitted[0] = s[0];
                splitted[1] = s[0];
            }
        }

        return splitted;
    }

}
