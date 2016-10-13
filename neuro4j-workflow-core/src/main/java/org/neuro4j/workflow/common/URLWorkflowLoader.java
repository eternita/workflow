package org.neuro4j.workflow.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public abstract class URLWorkflowLoader implements WorkflowLoader {

	@Override
	public WorkflowSource load(final String location) throws FlowExecutionException {
		URL resource = null;
		String path = normalize(location);
		try {
			resource = getResource(path);
			if (resource == null) {
				throw new FlowExecutionException(path + " not found.");
			}
			return new XMLWorkflowSource(path, resource);
		} catch (IOException e) {
			throw new FlowExecutionException(location, e);
		}

	}

	protected abstract URL getResource(String location) throws IOException;
	
	protected String normalize(String path){
		return path.replaceAll("\\.", File.separator);
	}

}
