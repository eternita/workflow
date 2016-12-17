package org.neuro4j.workflow.hystrix;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class DefaultHystrixBootstrap {
	private static final DynamicPropertyFactory dynamicPropertyFactory = DynamicPropertyFactory.getInstance();

	private static HystrixThreadPoolProperties.Setter defaultThreadPoolProperties = null;
	private static HystrixCommandProperties.Setter defaultCommandProperties = null;
	private static HystrixCommandGroupKey defaultGroupKey = HystrixCommandGroupKey.Factory.asKey("WorkflowDefaultGroupKey");

	public static synchronized void initializeHystrix() {

		if (defaultThreadPoolProperties != null)
			return;

		int threadPoolSize = dynamicPropertyFactory.getIntProperty("workflow.threadpool.size", 20).get();
		int queueSize = dynamicPropertyFactory.getIntProperty("workflow.queue.size", 50).get() * 2;

		defaultThreadPoolProperties = HystrixThreadPoolProperties.Setter().withCoreSize(threadPoolSize)
				.withMaxQueueSize(queueSize).withQueueSizeRejectionThreshold(queueSize / 2);

		int singleCommandTimeout = dynamicPropertyFactory.getIntProperty("workflow.max.wait", 3000).get();
		defaultCommandProperties = HystrixCommandProperties.Setter()
				.withExecutionIsolationThreadTimeoutInMilliseconds(singleCommandTimeout)
				.withExecutionIsolationThreadInterruptOnTimeout(true);

	}

	public static HystrixThreadPoolProperties.Setter getThreadPoolProperties() {
		if (defaultCommandProperties == null)
			initializeHystrix();
		return defaultThreadPoolProperties;
	}

	public static HystrixCommandProperties.Setter getCommandProperties() {
		if (defaultCommandProperties == null)
			initializeHystrix();
		return defaultCommandProperties;
	}

	public static HystrixCommandGroupKey getDefaultGroupKey() {
		if (defaultCommandProperties == null)
			initializeHystrix();
		return defaultGroupKey;
	}
}
