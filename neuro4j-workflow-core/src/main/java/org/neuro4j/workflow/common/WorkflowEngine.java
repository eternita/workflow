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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.async.ThreadPoolTaskExecutor.ThreadPoolTaskConfig;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.cache.WorkflowCache;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.neuro4j.workflow.node.FlowParameter;
import org.neuro4j.workflow.node.WorkflowProcessor;
import org.neuro4j.workflow.utils.Validation;

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
				                .withLoader(new ClasspathWorkflowLoader(new XmlWorkflowConverter())));
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
	
	public FutureTask<ExecutionResult> executeAsync(String flow) throws FlowExecutionException {
		return workflowProcessor.executeAsync(flow, new WorkflowRequest());
	}
	
	public FutureTask<ExecutionResult> executeAsync(String flow, Map<String, Object> params)
			throws FlowExecutionException {
		WorkflowRequest request = new WorkflowRequest();

		if (null != params && !params.isEmpty()) {
			for (String key : params.keySet())
				request.addParameter(key, params.get(key));
		}
		return workflowProcessor.executeAsync(flow, request);
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
		
		private ActionHandlersRegistry registry;
		
		private final WorkflowConverter converter;
		
		private final Map<String, FlowParameter> aliases = new HashMap<>();
		
		private ThreadPoolTaskConfig threadPoolTaskConfig;

		public ConfigBuilder() {
			converter = new XmlWorkflowConverter();
		}

		public ConfigBuilder withLoader(WorkflowLoader loader) {
			this.loader = loader;
			return this;
		}
		
		public ConfigBuilder withThreadPoolTaskConfig(ThreadPoolTaskConfig threadPoolTaskConfig) {
			this.threadPoolTaskConfig = threadPoolTaskConfig;
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
		
		public ConfigBuilder withActionRegistry(ActionHandlersRegistry registry) {
			this.registry = registry;
			return this;
		}

		public ConfigBuilder withAliases(final Map<String, String> aliases) throws FlowExecutionException {
			Validation.requireNonNull(aliases, () -> new FlowExecutionException("Aliases can not be null"));
			for (String key : aliases.keySet()) {
				FlowParameter param = FlowParameter.parse(aliases.get(key));
				this.aliases.put(key, param);
			}

			return this;
		}
		
		public WorkflowLoader getLoader() {
			loader = Optional.ofNullable(loader).orElse(new RemoteWorkflowLoader(converter, new ClasspathWorkflowLoader(converter)));
			return loader;
		}

		public CustomBlockInitStrategy getCustomInitStrategy() {
			customInitStrategy =  Optional.ofNullable(customInitStrategy).orElse(new DefaultCustomBlockInitStrategy());
			return customInitStrategy;
		}
		
		public WorkflowCache getWorkflowCache() {
			workflowCache = Optional.ofNullable(workflowCache).orElse(EmptyWorkflowCache.INSTANCE);
			return workflowCache;
		}
		
		public ActionHandlersRegistry getActionRegistry() {
			this.registry = Optional.ofNullable(this.registry).orElse(new ActionHandlersRegistry(Collections.emptyMap()));
			return this.registry;
		}
		
		public Map<String, FlowParameter> getAliases() {
			return this.aliases;
		}
		
		public ThreadPoolTaskConfig getThreadPoolTaskConfig() {
			threadPoolTaskConfig = Optional.ofNullable(threadPoolTaskConfig).orElse(new ThreadPoolTaskConfig());
			return threadPoolTaskConfig;
		}
	}

}

