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

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.async.ThreadPoolTaskExecutor.ThreadPoolTaskConfig;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 
 */
public class WorkflowConfigFactory {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowConfigFactory.class);
	
    public static ConfigBuilder load() {
    	ConfigBuilder configBuilder = new ConfigBuilder();
    	final Config neuro4jConfig = ConfigFactory.load().getConfig("neuro4j");

    	
    	// aliases
		final Config aliasesConfig = neuro4jConfig.getConfig("aliases");
		Map<String, String> aliases = new HashMap<String, String>();
		aliasesConfig.entrySet().forEach(e -> aliases.put(e.getKey().replace("\"", ""), (String) e.getValue().unwrapped()));

		try {
			configBuilder.withAliases(aliases);
		} catch (FlowExecutionException e1) {
			logger.error("Error loading aliases", e1);
		}
        
		
    	// pool
		final Config poolConfig = neuro4jConfig.getConfig("threadPoolTaskConfig");
		ThreadPoolTaskConfig threadPoolTaskConfig = new ThreadPoolTaskConfig();
		

		int corePoolSize = poolConfig.getInt("corePoolSize");
		threadPoolTaskConfig.setCorePoolSize(corePoolSize);
		
		int keepAliveSeconds = poolConfig.getInt("keepAliveSeconds");
		threadPoolTaskConfig.setKeepAliveSeconds(keepAliveSeconds);
		
		boolean allowCoreThreadTimeOut = poolConfig.getBoolean("allowCoreThreadTimeOut");
		threadPoolTaskConfig.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
		
		configBuilder.withThreadPoolTaskConfig(threadPoolTaskConfig);
		
		int maxPoolSize = poolConfig.getInt("maxPoolSize");
		threadPoolTaskConfig.setMaxPoolSize(maxPoolSize);
    	
		int queueCapacity = poolConfig.getInt("queueCapacity");
		threadPoolTaskConfig.setQueueCapacity(queueCapacity);
		
		// cache
		String cache = neuro4jConfig.getString("workflowCache");
		
		if (cache != null) {

			if ("emptyMap".equals(cache)) {
				configBuilder.withWorkflowCache(EmptyWorkflowCache.INSTANCE);
			} else if ("concurrentMap".equals(cache)) {
				configBuilder.withWorkflowCache(new ConcurrentMapWorkflowCache());
			}
		}
        return configBuilder;
    }
	


}