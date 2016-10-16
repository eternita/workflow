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

package org.neuro4j.workflow.common;

import java.io.IOException;
import java.net.URL;

/**
 * Loads workflow from classpath.
 *
 */
public class ClasspathWorkflowLoader extends URLWorkflowLoader{
	
	public final static String DEFAULT_EXT = ".n4j";
	
	private final String fileExt;
	
	public ClasspathWorkflowLoader(final String ext){
		this.fileExt = ext;
	}
	public ClasspathWorkflowLoader(){
		this(DEFAULT_EXT);
	}

	@Override
	protected URL getResource(String location) throws IOException {
		  return  getClass().getClassLoader().getResource(location + fileExt);
	}
}
