package org.neuro4j.workflow.hystrix;

import com.netflix.hystrix.HystrixCommandKey;

public abstract class AbstractHystrixCommand extends HystrixLifecycleCommand {


	protected AbstractHystrixCommand() {
		super(Setter.withGroupKey(DefaultHystrixBootstrap.getDefaultGroupKey())
				.andCommandKey(HystrixCommandKey.Factory.asKey("WorkflowDefaultCommandKey"))
				.andThreadPoolPropertiesDefaults(DefaultHystrixBootstrap.getThreadPoolProperties())
				.andCommandPropertiesDefaults(DefaultHystrixBootstrap.getCommandProperties()));
	}



}
