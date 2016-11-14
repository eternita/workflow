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
package org.neuro4j.workflow.node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.ActionHandler;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.async.CallableTask;
import org.neuro4j.workflow.async.ThreadPoolTaskExecutor;
import org.neuro4j.workflow.cache.ActionHandlersRegistry;
import org.neuro4j.workflow.cache.WorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.debug.DebugService;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.neuro4j.workflow.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes workflow with given parameters
 */
public class WorkflowProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkflowProcessor.class);
	

	private final ActionHandlersRegistry registry;

	private final WorkflowLoader loader;

	private final CustomBlockLoader customBlockLoader;
	
	private final WorkflowCache cache;
	
	private final Map<String, FlowParameter> aliases = new HashMap<>();
	
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * Constructor
	 * @param builder object with configuration
	 */
	public WorkflowProcessor(ConfigBuilder builder) {
		this.loader = builder.getLoader();
		this.customBlockLoader = new CustomBlockLoader(builder.getCustomInitStrategy());
		this.cache = builder.getWorkflowCache();
		this.registry = builder.getActionRegistry();
		this.aliases.putAll(builder.getAliases());
		this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor(builder.getThreadPoolTaskConfig());
	}

	/**
	 * Executes flow by name with request's parameters
	 * @param flow name
	 * @param request with parameters
	 * @return execution result
	 */
	public ExecutionResult execute(final String flow, final WorkflowRequest request) {

		long start = System.currentTimeMillis();

		ExecutionResult result = new ExecutionResult(request.getLogicContext());

		try {
			
			FlowParameter flowParameter = resolveFlow(flow);

			logger.debug("Loading flow: {}", flowParameter);
			long startLoading = System.currentTimeMillis();

			Workflow workflow = loadWorkflow(flowParameter.getFlowName());

			logger.debug("Loaded flow: {} in {} ms", flowParameter.getFlowName(), System.currentTimeMillis() - startLoading);
			if (null == workflow)
				throw new FlowExecutionException("Flow '" + flowParameter.getFlowName() + "' can't be loaded");

			StartNode startNode = workflow.getStartNode(flowParameter.getStartNode());
			if (null == startNode)
				throw new FlowExecutionException("StartNode '" + flowParameter.getStartNode() + "' not found in flow " + flowParameter.getFlowName());

			if (!workflow.isPublic()) {
				throw new FlowExecutionException("Flow '" + flowParameter.getFlowName() + "' is not public");
			}

			if (!startNode.isPublic()) {
				throw new FlowExecutionException("Node '" + startNode.getName() + "' is not public");
			}

			request.pushPackage(workflow.getPackage());

			executeWorkflow(startNode, request);
			request.popPackage();
		} catch (FlowExecutionException ex) {
			logger.error(ex.getMessage(), ex);
			result.setExecutionExeption(ex);
		}

		WorkflowNode lastNode = request.getLastSuccessfulNode();

		if (lastNode != null) {
			result.setLastSuccessfulNodeName(lastNode.getName());
		}
		logger.debug("Flow execution time: {} ms.", System.currentTimeMillis() - start);
		return result;
	}
	
	public FlowParameter resolveFlow(String name) throws FlowExecutionException {
		FlowParameter param = aliases.get(name);
		if (param != null) {
			logger.debug("Alias  {} resolved to {}", name, param.toString());
		} else {
			param = FlowParameter.parse(name);
		}
		return param;
	}


	/**
	 * Executes node with request's parameters
	 * @param firstNode node to be executed
	 * @param request with parameters
	 * @throws FlowExecutionException in case of error
	 */
	 final void executeWorkflow(WorkflowNode firstNode, WorkflowRequest request) throws FlowExecutionException {

		if (null == request)
			throw new RuntimeException("WorkflowRequest must not be null");

		WorkflowNode step = firstNode;

		while (null != step) {
			
			WorkflowNode lastNode = step;

			step = executeNode(step, request);

			request.setLastSuccessfulNode(lastNode);

			if (step != null) {
				logger.debug("Next step: {} ({})", step.getName(), step.getUuid());
			}

		}

	}

	public FutureTask<ExecutionResult> executeAsync(final String flow, final WorkflowRequest request)
			throws FlowExecutionException {
        Validation.requireNonNull(flow, ()-> new FlowExecutionException("Flow can not be null"));
        Validation.requireNonNull(request, ()-> new FlowExecutionException("WorkflowRequest can not be null"));
        
		Callable<ExecutionResult> callable = new CallableTask(this, flow, request);
		return threadPoolTaskExecutor.submit(callable);

	}

	/**
	 * Executes next node with given parameters
	 * @param request with parameters
	 * @return next workflow node
	 * @throws FlowExecutionException in case of error
	 */
	private final WorkflowNode executeNode(WorkflowNode node, WorkflowRequest request) throws FlowExecutionException {

		long startTime = System.currentTimeMillis();

		logger.debug("      Running: node {} ({})", node.getName(), node.getClass().getCanonicalName());

		node.validate(this, request.getLogicContext());

		DebugService.getInstance().onNodeCall(node, request);

		Transition transition = node.execute(this, request);

		logger.debug("      Finished: node {} in ({} ms.)", node.getName(), (System.currentTimeMillis() - startTime));
		if (transition != null) {
			return transition.getToNode();
		}

		return null;
	}

	/**
	 * Returns object implemented ActionBlock
	 * @param node CustomNode with executable class
	 * @return object implemented ActionBlock
	 * @throws FlowExecutionException
	 */
	ActionBlock loadCustomBlock(CustomNode node) throws FlowExecutionException {
		return customBlockLoader.lookupBlock(node);
	}

	/**
	 * Returns class object implemented ActionBlock
	 * @param node CustomNode with executable class
	 * @return class object implemented ActionBlock
	 * @throws FlowExecutionException
	 */
	Class<? extends ActionBlock> getCustomBlockClass(CustomNode node) throws FlowExecutionException {
		return customBlockLoader.getCustomBlockClass(node);
	}

	/**
	 * Loads workflow from cache
	 * @param flowName name
	 * @return flow object
	 * @throws FlowExecutionException in case of error
	 */
	public Workflow loadWorkflow(String flowName) throws FlowExecutionException {
		return cache.get(loader, flowName);
	}
	
	public ActionHandler getActionHandler(ActionBlock obj){
		return registry.get(obj.getClass());
	}

}
