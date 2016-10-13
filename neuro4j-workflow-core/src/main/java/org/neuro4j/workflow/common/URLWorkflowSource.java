package org.neuro4j.workflow.common;

import java.net.URL;

public class URLWorkflowSource implements WorkflowSource {

	protected URL resource;

	protected Long lastModified;

	private String location;

	public URLWorkflowSource(String location, URL resource) {
      this.location = location;
      this.resource = resource;
      
	}

	@Override
	public String location() {
		return location;
	}

	@Override
	public Workflow content() throws FlowExecutionException {
		return null;
	}

	@Override
	public long lastModified() {
		return 0;
	}

}
