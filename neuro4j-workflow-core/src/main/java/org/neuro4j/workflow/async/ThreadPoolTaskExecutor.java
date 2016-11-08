package org.neuro4j.workflow.async;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.utils.Validation;

public class ThreadPoolTaskExecutor {

	private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();

	private final ThreadPoolExecutor threadPoolExecutor;

	public ThreadPoolTaskExecutor(ThreadPoolTaskConfig config) {
		BlockingQueue<Runnable> queue = createQueue(config.getQueueCapacity());

		ThreadPoolExecutor executor;

		executor = new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaxPoolSize(),
				config.getKeepAliveSeconds(), TimeUnit.SECONDS, queue, config.getThreadFactory(),
				config.getRejectedExecutionHandler());

		if (config.isAllowCoreThreadTimeOut()) {
			executor.allowCoreThreadTimeOut(true);
		}

		this.threadPoolExecutor = executor;
	}

	private BlockingQueue<Runnable> createQueue(int queueCapacity) {
		if (queueCapacity > 0) {
			return new LinkedBlockingQueue<>(queueCapacity);
		} else {
			return new SynchronousQueue<>();
		}
	}

	private ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
		Validation.requireNonNull(this.threadPoolExecutor, () -> new IllegalStateException("ThreadPoolTaskExecutor not initialized"));
		return this.threadPoolExecutor;
	}

	public FutureTask<ExecutionResult> submit(Callable<ExecutionResult> callable) throws FlowExecutionException {

		FutureTask<ExecutionResult> task = new FutureTask<ExecutionResult>(callable);

		ExecutorService executor = getThreadPoolExecutor();
		try {
			FutureTask<ExecutionResult> future = new FutureTask<ExecutionResult>(callable);
			executor.execute(future);
			return future;
		} catch (RejectedExecutionException ex) {
			throw new FlowExecutionException("Executor [" + executor + "] did not accept task: " + task, ex);
		}
	}

	public static class ThreadPoolTaskConfig {

		private int corePoolSize = 1;

		private int maxPoolSize = Integer.MAX_VALUE;

		private int keepAliveSeconds = 60;

		private int queueCapacity = Integer.MAX_VALUE;

		private boolean allowCoreThreadTimeOut = false;

		private ThreadFactory threadFactory;
		
		private RejectedExecutionHandler rejectedExecutionHandler;

		public ThreadPoolTaskConfig() {

		}

		public int getCorePoolSize() {
			return corePoolSize;
		}

		public ThreadPoolTaskConfig setCorePoolSize(int corePoolSize) {
			this.corePoolSize = corePoolSize;
			return this;
		}

		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		public ThreadPoolTaskConfig setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
			return this;
		}

		public int getKeepAliveSeconds() {
			return keepAliveSeconds;
		}

		public ThreadPoolTaskConfig setKeepAliveSeconds(int keepAliveSeconds) {
			this.keepAliveSeconds = keepAliveSeconds;
			return this;
		}

		public int getQueueCapacity() {
			return queueCapacity;
		}

		public ThreadPoolTaskConfig setQueueCapacity(int queueCapacity) {
			this.queueCapacity = queueCapacity;
			return this;
		}

		public boolean isAllowCoreThreadTimeOut() {
			return allowCoreThreadTimeOut;
		}

		public ThreadPoolTaskConfig setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
			this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
			return this;
		}

		public ThreadFactory getThreadFactory() {
			threadFactory = Optional.ofNullable(threadFactory).orElse(Executors.defaultThreadFactory());
			return threadFactory;
		}

		public ThreadPoolTaskConfig setThreadFactory(ThreadFactory threadFactory) {
			this.threadFactory = threadFactory;
			return this;
		}

		public RejectedExecutionHandler getRejectedExecutionHandler() {
			rejectedExecutionHandler = Optional.ofNullable(rejectedExecutionHandler).orElse(defaultHandler);

			return rejectedExecutionHandler;
		}

		public ThreadPoolTaskConfig setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
			this.rejectedExecutionHandler = rejectedExecutionHandler;
			return this;
		}

	}

}
