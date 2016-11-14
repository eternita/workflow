package org.neuro4j.workflow.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.loader.WorkflowLoader;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * WorkflowLoader based on Guava Cache.
 *
 */
public class GuavaCachedWorkflowLoader implements WorkflowLoader{
	
	  private final WorkflowLoader delegate;

	  private final Cache<String, Workflow> cache;
	  

	public GuavaCachedWorkflowLoader(WorkflowLoader delegate, Cache<String, Workflow> cache) {
		super();
		this.delegate = delegate;
		this.cache = cache;
	}

	public static GuavaCachedWorkflowLoader cacheWithExpiration(final WorkflowLoader delegate, final long duration,
			final TimeUnit unit) {
		Cache<String, Workflow> cache = CacheBuilder.newBuilder().expireAfterAccess(duration, unit).build();
		return new GuavaCachedWorkflowLoader(delegate, cache);
	}


	@Override
	public Workflow load(final String name) throws FlowExecutionException {
		try {
			return cache.get(name, new Callable<Workflow>() {
				@Override
				public Workflow call() throws Exception {
					return delegate.load(name);
				}
			});
		} catch (ExecutionException e) {
			Throwables.propagateIfPossible(e.getCause(), FlowExecutionException.class);
			throw Throwables.propagate(e.getCause());
		}
	}

}
