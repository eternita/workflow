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
package org.neuro4j.workflow.common;

import java.io.Reader;

import org.neuro4j.workflow.loader.f4j.FlowConverter;
import org.neuro4j.workflow.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlWorkflowConverter implements WorkflowConverter{
	
	private static final Logger logger = LoggerFactory.getLogger(XmlWorkflowConverter.class);
	
	final String fileExt;
	
	public XmlWorkflowConverter(final String ext){
		this.fileExt = ext;
	}
	
	public XmlWorkflowConverter(){
		this(DEFAULT_EXT);
	}

	@Override
	public Workflow convert(Reader stream, String name) throws FlowExecutionException {
		Validation.requireNonNull(stream, () -> new FlowExecutionException("InputStream can not be null"));
		
		logger.debug("Converting workflow {} from xml to java object", name);
		
		return FlowConverter.xml2workflow(stream, name);
	}

	@Override
	public String getFileExt() {
		return this.fileExt;
	}
	
	

}
