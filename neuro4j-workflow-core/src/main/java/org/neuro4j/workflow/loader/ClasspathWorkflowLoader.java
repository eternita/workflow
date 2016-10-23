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
package org.neuro4j.workflow.loader;

import java.io.IOException;
import java.net.URL;

import org.neuro4j.workflow.common.WorkflowConverter;

/**
 * Loads workflow from classpath.
 *
 */
public class ClasspathWorkflowLoader extends URLWorkflowLoader{
	
	
	public ClasspathWorkflowLoader(final WorkflowConverter converter){
		super(converter);
	}

	@Override
	protected URL getResource(String name) throws IOException {
		  String location = normalize(name);
		  return  getClass().getClassLoader().getResource(location + converter.getFileExt());
	}
	
}
