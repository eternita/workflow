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

	static{
		getConfigurationForCommand(DEFAULT_GROUP_KEY);		
	}


	public static class CommandConfiguration {

		int threadPoolSize;
		int queueSize;
		int singleCommandTimeout;

		private final HystrixCommandGroupKey groupKey;

		private HystrixThreadPoolProperties.Setter defaultThreadPoolProperties = null;
		private HystrixCommandProperties.Setter defaultCommandProperties = null;

		CommandConfiguration(String groupName) {
			groupKey = HystrixCommandGroupKey.Factory.asKey(DEFAULT_GROUP_KEY);
		}

		public int getThreadPoolSize() {
			return threadPoolSize;
		}

		public void setThreadPoolSize(int threadPoolSize) {
			this.threadPoolSize = threadPoolSize;
		}

		public int getQueueSize() {
			return queueSize;
		}

		public void setQueueSize(int queueSize) {
			this.queueSize = queueSize;
		}

		public int getSingleCommandTimeout() {
			return singleCommandTimeout;
		}

		public void setSingleCommandTimeout(int singleCommandTimeout) {
			this.singleCommandTimeout = singleCommandTimeout;
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
				.withExecutionIsolationThreadInterruptOnTimeout(true);

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
