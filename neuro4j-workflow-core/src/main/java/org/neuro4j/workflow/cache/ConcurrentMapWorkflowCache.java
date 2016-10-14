package org.neuro4j.workflow.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ConcurrentMapWorkflowCache implements WorkflowCache {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ConcurrentMap<String, Workflow> cache;

	protected ConcurrentMapWorkflowCache(final ConcurrentMap<String, Workflow> cache) {
		this.cache = cache;
	}

	public ConcurrentMapWorkflowCache() {
		this(new ConcurrentHashMap<String, Workflow>());
	}

	@Override
	public void clearAll() {
		cache.clear();
	}

	@Override
	public Workflow get(WorkflowLoader loader, String location) throws FlowExecutionException {
		Workflow entry = cache.get(location);
		if (entry == null) {
			logger.debug("Loading: {}", location);
			entry = EmptyWorkflowCache.INSTANCE.get(loader, location);
			cache.put(location, entry);
		} else {
			logger.debug("Found in cache: {}", location);
		}
		return entry;
	}

	@Override
	public void clear(String key) {
		cache.remove(key);
	}

}