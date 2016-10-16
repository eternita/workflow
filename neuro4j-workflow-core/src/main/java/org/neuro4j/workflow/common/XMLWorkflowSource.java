package org.neuro4j.workflow.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.neuro4j.workflow.loader.f4j.FlowConverter;
import org.neuro4j.workflow.log.Logger;

/**
 * Converts stream from xml-resource  to workflow object
 */
public class XMLWorkflowSource extends URLWorkflowSource {

	public XMLWorkflowSource(String name, URL resource) {
		super(name, resource);
	}

	@Override
	public Workflow content() throws FlowExecutionException {
		Workflow net = null;
		InputStream inputStream = null;
		try {
			inputStream = getStream();
			if (null != inputStream)
				net = FlowConverter.xml2workflow(inputStream, name());
		} catch (Exception e) {
			throw new FlowExecutionException(e);
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

	protected InputStream getStream() throws IOException {
		return resource.openStream();
	}

}
