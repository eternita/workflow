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

package org.neuro4j.workflow.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache implementation based on ConcurrentMap
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