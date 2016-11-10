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
package org.neuro4j.workflow.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.neuro4j.workflow.cache.WorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Cache implementation based on GuavaCache
 */
public class GuavaWorkflowCache implements WorkflowCache {

	private static final Logger logger = LoggerFactory.getLogger(GuavaWorkflowCache.class);

	private final Cache<String, Workflow> cache;

	public static GuavaWorkflowCache cacheWithExpiration(final long duration, final TimeUnit unit) {
		Cache<String, Workflow> cache = CacheBuilder.newBuilder().expireAfterAccess(duration, unit).build();
		return new GuavaWorkflowCache(cache);
	}

	private GuavaWorkflowCache(final Cache<String, Workflow> cache) {
		this.cache = cache;
	}

	@Override
	public void clearAll() {
		cache.invalidateAll();
	}

	@Override
	public Workflow get(final WorkflowLoader loader, final String location) throws FlowExecutionException {

		Workflow workflow = null;
		try {
			workflow = cache.get(location, new Callable<Workflow>() {
				@Override
				public Workflow call() throws FlowExecutionException {
					logger.debug("Loading to cache: {}", location);
					return loader.load(location);
				}
			});
		} catch (ExecutionException e) {
			throw new FlowExecutionException(e);
		}

		return workflow;
	};

	@Override
	public void clear(String key) {
		cache.invalidate(key);
	}

}