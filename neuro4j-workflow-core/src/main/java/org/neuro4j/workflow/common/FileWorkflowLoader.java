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

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Loads workflow from external folder.
 *
 */
public class FileWorkflowLoader extends URLWorkflowLoader{
	
	final URLWorkflowLoader delegate;
	final String baseDir;

	public FileWorkflowLoader(URLWorkflowLoader loader, String baseDir){
		this.delegate = loader;
		this.baseDir = baseDir;
	}
	
	public FileWorkflowLoader(final String baseDir, final String ext){
		this(new ClasspathWorkflowLoader(ext), baseDir);
	}
	
	@Override
	protected URL getResource(final String location) throws IOException {
	    File file = new File(location);
	    return file.exists() ? file.toURI().toURL() : delegate.getResource(location);
	}

}
