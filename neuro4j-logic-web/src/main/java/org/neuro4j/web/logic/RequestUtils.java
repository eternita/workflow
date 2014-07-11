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

package org.neuro4j.web.logic;

public class RequestUtils
{

    // from jstl lib (c:redirect)
    public static boolean isAbsoluteUrl(String url)
    {
        if (url == null)
            return false;
        int colonPos;
        if ((colonPos = url.indexOf(":")) == -1) {
            return false;
        }

        for (int i = 0; i < colonPos; ++i) {
            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+.-".indexOf(url.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }
}
