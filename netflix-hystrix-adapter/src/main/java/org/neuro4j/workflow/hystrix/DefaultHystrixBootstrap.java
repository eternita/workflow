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

package org.neuro4j.workflow.hystrix;

import com.netflix.hystrix.HystrixCommand.Setter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class DefaultHystrixBootstrap {

	private static final ConcurrentMap<String, CommandConfiguration> cache = new ConcurrentHashMap<>();

	public static final String DEFAULT_GROUP_KEY = "WorkflowDefaultGroupKey";
	public static final String DEFAULT_COMMAND_KEY = "WorkflowDefaultCommandKey";

	final static Config neuro4jConfig = ConfigFactory.load().getConfig("neuro4j").getConfig("hystrix");


	public static class CommandConfiguration {


		private final HystrixCommandGroupKey groupKey;

		private HystrixThreadPoolProperties.Setter defaultThreadPoolProperties = null;
		private HystrixCommandProperties.Setter defaultCommandProperties = null;

		CommandConfiguration(String groupName) {
			groupKey = HystrixCommandGroupKey.Factory.asKey(groupName);
		}


		public HystrixThreadPoolProperties.Setter getDefaultThreadPoolProperties() {
			return defaultThreadPoolProperties;
		}

		public void setDefaultThreadPoolProperties(HystrixThreadPoolProperties.Setter defaultThreadPoolProperties) {
			this.defaultThreadPoolProperties = defaultThreadPoolProperties;
		}

		public HystrixCommandProperties.Setter getDefaultCommandProperties() {
			return defaultCommandProperties;
		}

		public void setDefaultCommandProperties(HystrixCommandProperties.Setter defaultCommandProperties) {
			this.defaultCommandProperties = defaultCommandProperties;
		}

		public HystrixCommandGroupKey getGroupKey() {
			return groupKey;
		}

	}

	public static CommandConfiguration getConfigurationForCommand(String name) {

		CommandConfiguration configuration = cache.get(name);

		if (configuration != null) {
			return configuration;
		}

		Config commandDefaultConfig = neuro4jConfig.getConfig(name);

		if (commandDefaultConfig == null){
			commandDefaultConfig = neuro4jConfig.getConfig(DEFAULT_GROUP_KEY);
		}
		
		configuration = new CommandConfiguration(name);

		int threadPoolSize = commandDefaultConfig.getInt("threadpoolSize");
		int queueSize = commandDefaultConfig.getInt("queueSize");

		HystrixThreadPoolProperties.Setter defaultThreadPoolProperties = HystrixThreadPoolProperties.Setter()
				.withCoreSize(threadPoolSize).withMaxQueueSize(queueSize)
				.withQueueSizeRejectionThreshold(queueSize / 2);

		configuration.setDefaultThreadPoolProperties(defaultThreadPoolProperties);

		int singleCommandTimeout = commandDefaultConfig.getInt("maxWait");

		HystrixCommandProperties.Setter defaultCommandProperties = HystrixCommandProperties.Setter()
				.withExecutionTimeoutInMilliseconds(singleCommandTimeout)
				.withExecutionTimeoutEnabled(true);

		configuration.setDefaultCommandProperties(defaultCommandProperties);

		cache.put(name, configuration);

		return configuration;

	}

	public static Setter getSetterForGroup(String groupName, String commandName) {

		CommandConfiguration configuration = getConfigurationForCommand(groupName);

		return Setter.withGroupKey(configuration.getGroupKey())
				.andCommandKey(HystrixCommandKey.Factory.asKey(commandName))
				.andThreadPoolPropertiesDefaults(configuration.getDefaultThreadPoolProperties())
				.andCommandPropertiesDefaults(configuration.getDefaultCommandProperties());

	}

}
