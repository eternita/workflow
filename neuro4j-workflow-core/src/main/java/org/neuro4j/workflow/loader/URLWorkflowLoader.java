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
import org.neuro4j.workflow.log.Logger;

public abstract class URLWorkflowLoader implements WorkflowLoader {
	
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
			Logger.error(this, e);
		} finally {
			try {
				if (null != inputStream)
					inputStream.close();
			} catch (Exception e) {
				Logger.error(this, e);
			}
		}
		return net;
	}

	protected abstract URL getResource(String location) throws IOException;
	
	protected String normalize(String path){
		return path.replaceAll("\\.", File.separator);
	}
	
	protected Reader getReader(URL resource) throws IOException {
		return new InputStreamReader(resource.openStream(), "UTF-8");
	}

}
