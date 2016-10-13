package org.neuro4j.workflow.common;

import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

public class RemoteWorkflowSource extends URLWorkflowSource{

	private Consumer<URLConnection> connectionConsumer;
	
	public RemoteWorkflowSource(String location, URL resource) {
		super(location, resource);
	}


}
