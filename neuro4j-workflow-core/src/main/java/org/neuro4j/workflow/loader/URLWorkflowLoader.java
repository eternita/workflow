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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Optional;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class URLWorkflowLoader implements WorkflowLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(URLWorkflowLoader.class);
	
	protected WorkflowConverter converter;
	
	public URLWorkflowLoader(final WorkflowConverter converter){
		this.converter = Optional.ofNullable(converter).orElse(new XmlWorkflowConverter());
	}
	
	@Override
	public Workflow load(final String name) throws FlowExecutionException {
		try {
			URL resource = getResource(name);
			if (resource == null) {
				throw new FlowExecutionException(name + " not found.");
			}
			return content(resource, name);
		} catch (IOException e) {
			throw new FlowExecutionException(name, e);
		}
	}
	

	protected Workflow content(URL resource, final String name) throws FlowExecutionException {
		Workflow net = null;
		Reader inputStream = null;
		try {
			inputStream = getReader(resource);
			net = converter.convert(inputStream, name);
		
		} catch (IOException e) {
			logger.error("Error during loading " + resource, e);
		} finally {
			try {
				if (null != inputStream)
					inputStream.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return net;
	}

	protected abstract URL getResource(String location) throws IOException;
	
	protected String normalize(String path){
		return path.replace(".", File.separator);
	}
	
	protected Reader getReader(URL resource) throws IOException {
		return new InputStreamReader(resource.openStream(), "UTF-8");
	}

}
