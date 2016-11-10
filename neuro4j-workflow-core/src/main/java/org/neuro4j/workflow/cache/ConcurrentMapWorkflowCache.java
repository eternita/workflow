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
package org.neuro4j.workflow.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.function.Function;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache implementation based on ConcurrentHashMap
 */
public class ConcurrentMapWorkflowCache implements WorkflowCache {

	private static final Logger logger = LoggerFactory.getLogger(ConcurrentMapWorkflowCache.class);

	private final ConcurrentMap<String, WorkflowProxy> cache;

	public ConcurrentMapWorkflowCache(final ConcurrentMap<String, WorkflowProxy> cache) {
		this.cache = cache;
	}

	public ConcurrentMapWorkflowCache() {
		this(new ConcurrentHashMap<String, WorkflowProxy>());
	}

	@Override
	public void clearAll() {
		cache.clear();
	}

	@Override
	public Workflow get(final WorkflowLoader loader, final String location) throws FlowExecutionException {
		WorkflowProxy entry = cache.computeIfAbsent(location, new Function<String, WorkflowProxy>() {

			@Override
			public WorkflowProxy apply(String location) {
				return new WorkflowProxy(location);
			}
		});
		return entry.get(loader);
	}

	@Override
	public void clear(String key) {
		cache.remove(key);
	}
	
	
	public class WorkflowProxy{
		
		private Object lock = new Object();
		private Workflow workflow;
		private String location;
		
		public WorkflowProxy(String location){
			this.location = location;
		}
		
		public Workflow get(WorkflowLoader loader) throws FlowExecutionException{
			if (workflow != null){
				logger.debug("Found in cache: {}", location);
				return workflow;
			}
			synchronized (lock) {
				if (workflow != null){
					logger.debug("Found in cache: {}", location);
					return workflow;
				}
				logger.debug("Loading to cache: {}", location);
				workflow = loader.load(location);
			}
			return workflow;
		}
		
	}

	
}