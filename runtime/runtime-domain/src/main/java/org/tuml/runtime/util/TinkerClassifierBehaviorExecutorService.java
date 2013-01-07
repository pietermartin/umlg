package org.tuml.runtime.util;

import org.tuml.runtime.domain.IClassifierEvent;

import java.util.concurrent.*;

public class TinkerClassifierBehaviorExecutorService {

	public static TinkerClassifierBehaviorExecutorService INSTANCE = new TinkerClassifierBehaviorExecutorService();
	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executorService);
	
	private TinkerClassifierBehaviorExecutorService() {
		super();
	}
	
	public void shutdown() {
		executorService.shutdown();
	}
	
	public Future<Boolean> submit(IClassifierEvent cse) {
		return completionService.submit(cse);
	}

	public Boolean take() {
		try {
			Future<Boolean> f =  this.completionService.take();
			return f.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void waitForCompletion(long timeout, TimeUnit unit) {
		try {
			this.executorService.awaitTermination(timeout, unit);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
