package org.neuro4j.workflow.node;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.cache.WorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowLoader;
import org.neuro4j.workflow.debug.DebugService;
import org.neuro4j.workflow.log.Logger;

/**
 *
 */
public class WorkflowProcessor {

	private final WorkflowLoader loader;

	private final CustomBlockLoader customBlockLoader;
	
	private final WorkflowCache cache;

	/**
	 * @param builder
	 */
	public WorkflowProcessor(ConfigBuilder builder) {
		this.loader = builder.getLoader();
		this.customBlockLoader = new CustomBlockLoader(builder.getCustomInitStrategy());
		this.cache = builder.getWorkflowCache();
	}

	/**
	 * @param flow
	 * @param request
	 * @return
	 */
	public ExecutionResult execute(final String flow, final WorkflowRequest request) {

		long start = System.currentTimeMillis();

		ExecutionResult result = new ExecutionResult(request.getLogicContext());

		try {
			String[] array = parseFlowName(flow);
			String flowName = array[0];
			String startNodeName = array[1];

			Logger.debug(this, "Loading flow: {}", flowName);
			long startLoading = System.currentTimeMillis();

			Workflow workflow = loadWorkflow(flowName);

			Logger.debug(this, "Loaded flow: {} in {} ms", flowName, System.currentTimeMillis() - startLoading);
			if (null == workflow)
				throw new FlowExecutionException("Flow '" + flowName + "' can't be loaded");

			StartNode startNode = workflow.getStartNode(startNodeName);
			if (null == startNode)
				throw new FlowExecutionException("StartNode '" + startNodeName + "' not found in flow " + flowName);

			if (!workflow.isPublic()) {
				throw new FlowExecutionException("Flow '" + flow + "' is not public");
			}

			if (!startNode.isPublic()) {
				throw new FlowExecutionException("Node '" + startNode.getName() + "' is not public");
			}

			request.pushPackage(workflow.getPackage());

			executeWorkflow(startNode, request);
			request.popPackage();
		} catch (FlowExecutionException ex) {
			Logger.error(this, ex.getMessage(), ex);
			result.setExecutionExeption(ex);
		}

		WorkflowNode lastNode = request.getLastSuccessfulNode();

		if (lastNode != null) {
			result.setLastSuccessfulNodeName(lastNode.getName());
		}
		Logger.debug(this, "Flow execution time: {} ms.", System.currentTimeMillis() - start);
		return result;
	}


	/**
	 * @param firstNode
	 * @param request
	 * @throws FlowExecutionException
	 */
	public final void executeWorkflow(WorkflowNode firstNode, WorkflowRequest request) throws FlowExecutionException {

		if (null == request)
			throw new RuntimeException("WorkflowRequest must not be null");

		WorkflowNode step = firstNode;

		while (null != step) {
			WorkflowNode lastNode = step;

			step = executeNode(step, request);

			request.setLastSuccessfulNode(lastNode);

			if (step != null) {
				Logger.debug(Workflow.class, "Next step: {} ({})", step.getName(), step.getUuid());
			}

		}

	}

	/**
	 * @param flow
	 * @return
	 * @throws FlowExecutionException
	 */
	public static String[] parseFlowName(String flow) throws FlowExecutionException {

		if (flow == null)
			throw new FlowExecutionException("Flow is undefined.");

		String[] array = flow.split("-");

		if (array.length != 2) {
			throw new FlowExecutionException("Incorrect flow name. Must be package.name.FlowName-StartNode");
		}

		array[0] = array[0].replace('.', '/');

		return array;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws FlowExecutionException
	 */
	private final WorkflowNode executeNode(WorkflowNode node, WorkflowRequest request) throws FlowExecutionException {

		long startTime = System.currentTimeMillis();

		Logger.debug(this, "      Running: {} ({})", node.getName(), this.getClass().getCanonicalName());

		node.validate(this, request.getLogicContext());

		DebugService.getInstance().onNodeCall(node, request);

		Transition transition = node.execute(this, request);

		Logger.debug(this, "      Finished: {} in ({} ms.)", node.getName(), (System.currentTimeMillis() - startTime));
		if (transition != null) {
			return transition.getToNode();
		}

		return null;
	}

	/**
	 * @param node
	 * @return
	 * @throws FlowInitializationException
	 */
	ActionBlock loadCustomBlock(CustomNode node) throws FlowInitializationException {
		return customBlockLoader.lookupBlock(node);
	}

	/**
	 * @param node
	 * @return
	 * @throws FlowInitializationException
	 */
	Class<? extends ActionBlock> getCustomBlockClass(CustomNode node) throws FlowInitializationException {
		return customBlockLoader.getCustomBlockClass(node);
	}

	public Workflow loadWorkflow(String flowName) throws FlowExecutionException {
		return cache.get(loader, flowName);
	}

}
