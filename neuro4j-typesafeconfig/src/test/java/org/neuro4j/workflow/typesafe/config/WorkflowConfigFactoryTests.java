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
package org.neuro4j.workflow.typesafe.config;

import static org.junit.Assert.*;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.neuro4j.workflow.async.ThreadPoolTaskExecutor.ThreadPoolTaskConfig;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.node.FlowParameter;

/**
 * 
 */
public class WorkflowConfigFactoryTests {

	

	@Test
	public void testWorkflowConfigFactory() throws FlowExecutionException{
		ConfigBuilder configBuilder = WorkflowConfigFactory.load();
		assertNotNull(configBuilder);
		assertEquals(configBuilder.getAliases().get("myflow").getFlowName(), FlowParameter.parse("org.neuro4j.MyFlow-Start").getFlowName());
		assertEquals(configBuilder.getAliases().get("myflow2").getFlowName(), FlowParameter.parse("org.neuro4j.workflow.MyFlow-StartNode2").getFlowName());
		assertEquals(configBuilder.getAliases().get("/homepage.htm").getFlowName(), FlowParameter.parse("org.neuro4j.workflow.HomePage-Start").getFlowName());

		// test pool
		ThreadPoolTaskConfig threadPoolTaskConfig =	configBuilder.getThreadPoolTaskConfig();
		assertEquals(threadPoolTaskConfig.getCorePoolSize(), 4);
		assertEquals(threadPoolTaskConfig.getKeepAliveSeconds(), 65);
		assertEquals(threadPoolTaskConfig.isAllowCoreThreadTimeOut(), false);
		assertEquals(threadPoolTaskConfig.getMaxPoolSize(), Integer.MAX_VALUE);
		assertEquals(threadPoolTaskConfig.getQueueCapacity(), Integer.MAX_VALUE);
	  
		// test cache
		
		assertThat(configBuilder.getWorkflowCache(), IsInstanceOf.instanceOf(EmptyWorkflowCache.class));
	}
	
	

}