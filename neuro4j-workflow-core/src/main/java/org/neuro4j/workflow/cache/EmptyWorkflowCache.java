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

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowLoader;
import org.neuro4j.workflow.common.WorkflowSource;

/**
 * Default cache implementation
 *
 */
public enum EmptyWorkflowCache implements WorkflowCache {
	INSTANCE;


	@Override
	public Workflow get(WorkflowLoader loader,  String flowName) throws  FlowExecutionException {
		WorkflowSource workflowSource = loader.load(flowName);
		if (workflowSource == null) {
			throw new FlowExecutionException("Workflow " + flowName + " not loaded");
		}

		Workflow workflow = workflowSource.content();
		return workflow;
	}

	@Override
	public void clearAll() {
	}

	@Override
	public void clear(String key) {
	}


}
