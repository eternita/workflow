package org.neuro4j.workflow.common;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteWorkflowLoader implements WorkflowLoader {

	final WorkflowLoader delegate;

	public RemoteWorkflowLoader(WorkflowLoader loader) {
		this.delegate = loader;
	}

	@Override
	public WorkflowSource load(final String location) throws FlowExecutionException {
		if (location.startsWith("http://") || location.startsWith("https://")) {
			try {

				return new RemoteWorkflowSource(location, new URL(location));
			} catch (MalformedURLException e) {
				throw new FlowExecutionException(location, e);
			}
		}
		return delegate.load(location);
	}

}
