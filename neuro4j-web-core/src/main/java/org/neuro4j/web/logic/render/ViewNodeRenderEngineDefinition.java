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

package org.neuro4j.web.logic.render;

/**
 * This interface allows define  render which will process View. It can be jsp, json or others formats.
 * Current interface will be used by Studio to define render type. 
 */
public abstract class ViewNodeRenderEngineDefinition {

    /**
     * Returns render name. This name will be displayed for ViewNode during editing in Studio.
     * @return
     */
    public abstract String getName();

    /**
     * Returns file ext. for Studio. Ex. "jsp", "xhtml"
     * @return
     */
    public abstract String getFileExt();

    /**
     * Return filter pattern for Studio. Ex "/WEB-INF/*" for web applications.
     * @return
     */
    public abstract String getPathFilter();
    

}
