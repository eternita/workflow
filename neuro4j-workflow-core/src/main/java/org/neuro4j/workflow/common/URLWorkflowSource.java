package org.neuro4j.workflow.common;

import java.net.URL;

public class URLWorkflowSource implements WorkflowSource {

	protected URL resource;

	protected Long lastModified;

	private String name;

	public URLWorkflowSource(String name, URL resource) {
      this.name = name;
      this.resource = resource;
      
	}

	@Override
	public String name() {
		return name;
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
