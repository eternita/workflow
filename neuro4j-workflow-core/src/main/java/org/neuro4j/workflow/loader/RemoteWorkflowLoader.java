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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;
import java.util.function.Function;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows to load workflow file from remote host.
 *
 */
public class RemoteWorkflowLoader extends URLWorkflowLoader {

	private static final Logger logger = LoggerFactory.getLogger(RemoteWorkflowLoader.class);
	
	/**
	 * If requested flow does not start with http:// or https:// delegates execution to next loader
	 */
	private final WorkflowLoader delegate;
    
	/**
     * Accepts connection before remote call. Can be used to set up connection parameter like timeout or headers
     */
    private final Consumer<URLConnection> connectionConsumer;
   
    /**
     * Calls this function to transform initial requested location before delegation. Can be used to transform call http://mydomain.com/mycontext/com.mypackage.Flow1 to com.mypackage.Flow1
     */
    private Function<String, String> onErrorRewrite;
    
	public RemoteWorkflowLoader(final WorkflowConverter converter, final WorkflowLoader delegate, Consumer<URLConnection> connectionConsumer, Function<String, String> onError) {
		super(converter);
		this.delegate = delegate;
		this.connectionConsumer = connectionConsumer;
		this.onErrorRewrite = onError;
	}
	
	public RemoteWorkflowLoader(final WorkflowConverter converter, final WorkflowLoader delegate) {
		this(converter, delegate, s->{}, s -> s);
	}

	@Override
	public Workflow load(final String location) throws FlowExecutionException {
		Validation.requireNonNull(location, () -> new FlowExecutionException("Location can not be null"));
		String flow = location;
		if (location.startsWith("http://") || location.startsWith("https://")) {
			try {
				URL url = getResource(location);
				flow = url.getFile();
				return content(url, location);
			} catch (Exception e) {
				logger.error("Error during loading " + location, e);
				flow = onErrorRewrite.apply(location);
				
			}
		}
		return delegate.load(flow);
	}

	@Override
	protected URL getResource(String location) throws IOException {
		return new URL(location);
	}
	
	protected Reader getReader(URL resource) throws IOException {
        URLConnection connection = resource.openConnection();     
        connectionConsumer.accept(connection);
        return new InputStreamReader(connection.getInputStream(), "UTF-8");
     }
}
