/*
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuro4j.workflow.common;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.cache.WorkflowCache;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.node.WorkflowProcessor;

/**
 * <p>
 * WorkflowEngine allows to execute workflow with some input parameters
 * </p>
 * <h2>
 * Getting Started:</h2>
 *
 * <pre>
 * WorkflowEngine engine = new WorkflowEngine();
 * ExecutionResult result = engine.execute("org.domain.workflow.SomeFlow-StartNode");
 * </pre>
 */
public class WorkflowEngine {

	private final WorkflowProcessor workflowProcessor;

	public WorkflowEngine(ConfigBuilder builder) {
		this.workflowProcessor = new WorkflowProcessor(builder);
	}
	
	/**
	 * Default constructor with  ConcurrentMapWorkflowCache and ClasspathLoader 
	 */
	public WorkflowEngine() {
		this(new ConfigBuilder().withCustomBlockInitStrategy(new DefaultCustomBlockInitStrategy())
				                .withWorkflowCache(new ConcurrentMapWorkflowCache())
				                .withLoader(new ClasspathWorkflowLoader()));
	}
	

	/**
	 * Constructor with  WorkflowLoader 
	 * @param loader can be composite of Remote/File/Classpath/Cache loaders
	 */
	public WorkflowEngine(WorkflowLoader loader) {
		this(new ConfigBuilder().withCustomBlockInitStrategy(new DefaultCustomBlockInitStrategy())
				                .withWorkflowCache(new ConcurrentMapWorkflowCache())
				                .withLoader(loader));
	}

	/**
	 * Executes flow with default parameters
	 * @param flow name
	 * @return execution result
	 */
	public ExecutionResult execute(String flow) {
		return execute(flow, new HashMap<String, Object>());
	}

	/**
	 * Executes flow with default parameters
	 * @param flow name
	 * @param params input parameters
	 * @return execution result
	 */
	public ExecutionResult execute(String flow, Map<String, Object> params) {
		WorkflowRequest request = new WorkflowRequest();

		if (null != params && !params.isEmpty()) {
			for (String key : params.keySet())
				request.addParameter(key, params.get(key));
		}
		return workflowProcessor.execute(flow, request);
	}

	/**
	 * Executes flow with given request
	 * @param flow name
	 * @param request object
	 * @return execution result
	 */
	public ExecutionResult execute(String flow, WorkflowRequest request) {
		return workflowProcessor.execute(flow, request);
	}

	/**
	 * Config class for engine
	 */
	public static class ConfigBuilder {

		private WorkflowLoader loader;

		private CustomBlockInitStrategy customInitStrategy;
		
		private WorkflowCache workflowCache;

		public ConfigBuilder() {

		}

		public ConfigBuilder withLoader(WorkflowLoader loader) {
			this.loader = loader;
			return this;
		}

		public ConfigBuilder withCustomBlockInitStrategy(CustomBlockInitStrategy customInitStrategy) {
			this.customInitStrategy = customInitStrategy;
			return this;
		}
		
		public ConfigBuilder withWorkflowCache(WorkflowCache cache) {
			this.workflowCache = cache;
			return this;
		}

		public WorkflowLoader getLoader() {
			return loader != null ? loader : new RemoteWorkflowLoader(new ClasspathWorkflowLoader());
		}

		public CustomBlockInitStrategy getCustomInitStrategy() {
			return customInitStrategy != null ? customInitStrategy : new DefaultCustomBlockInitStrategy();
		}
		
		public WorkflowCache getWorkflowCache() {
			return workflowCache != null ? workflowCache : EmptyWorkflowCache.INSTANCE;
		}

	}

}
