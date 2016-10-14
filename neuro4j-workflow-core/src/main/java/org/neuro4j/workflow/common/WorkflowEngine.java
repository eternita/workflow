package org.neuro4j.workflow.common;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.cache.WorkflowCache;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.node.WorkflowProcessor;

/**
 *
 */
public class WorkflowEngine {

	private final WorkflowProcessor workflowProcessor;

	/**
	 * @param builder
	 */
	public WorkflowEngine(ConfigBuilder builder) {
		this.workflowProcessor = new WorkflowProcessor(builder);
	}

	/**
	 * @param flow
	 * @return
	 */
	public ExecutionResult execute(String flow) {
		return execute(flow, new HashMap<String, Object>());
	}

	/**
	 * @param flow
	 * @param params
	 * @return
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
	 * @param flow
	 * @param request
	 * @return
	 */
	public ExecutionResult execute(String flow, WorkflowRequest request) {
		return workflowProcessor.execute(flow, request);
	}

	/**
	 *
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
