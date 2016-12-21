package org.neuro4j.workflow.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.Collections;
import java.util.List;

import org.neuro4j.workflow.FlowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class HystrixLifecycleCommand extends HystrixCommand<Integer> {

	private static Logger logger = LoggerFactory.getLogger(HystrixLifecycleCommand.class);
	
    private final List<HystrixLifecycleListener> lifecycleListeners;

    private FlowContext ctx;
    
    protected HystrixLifecycleCommand(HystrixCommandGroupKey group) {
        super(group);
        lifecycleListeners = setupListeners();
        notifyListenerOfConstruct();
    }

    protected HystrixLifecycleCommand(Setter setter) {
        super(setter);
        lifecycleListeners = setupListeners();
        notifyListenerOfConstruct();
    }

    protected void notifyListenerOfConstruct() {
        for (HystrixLifecycleListener listener: getLifecycleListenersInternal()){
            listener.onConstruct();
        }
    }


    protected void notifyListenerOfCommandStart() {
        for (HystrixLifecycleListener listener: getLifecycleListenersInternal()){
            listener.onCommandStart();
        }
    }


    protected void notifyListenerOfCommandComplete() {
        for (HystrixLifecycleListener listener: getLifecycleListenersInternal()){
            listener.onCommandComplete();
        }
    }


    protected List<HystrixLifecycleListener> getLifecycleListeners(){return Collections.emptyList();}

    private List<HystrixLifecycleListener> getLifecycleListenersInternal(){
        return lifecycleListeners;
    }

    private List<HystrixLifecycleListener> setupListeners(){
        List<HystrixLifecycleListener> lifecycleListeners = getLifecycleListeners();
        if (lifecycleListeners == null){
            lifecycleListeners = Collections.emptyList();
        }
        return lifecycleListeners;
    }


    @Override
    protected Integer run() throws Exception {
    	logger.debug("Starting command {}", this.getClass().getSimpleName());
    	int result;
        notifyListenerOfCommandStart();
        try {
            result = executeInternal(this.ctx);
        } finally {
            notifyListenerOfCommandComplete();
        }
        logger.debug("Finished command {}", this.getClass().getSimpleName());
        return result;
    }


    protected void setFlowContext(final FlowContext ctx) {
		this.ctx = ctx;
	}

	protected abstract Integer executeInternal(FlowContext ctx) throws Exception;
}
	