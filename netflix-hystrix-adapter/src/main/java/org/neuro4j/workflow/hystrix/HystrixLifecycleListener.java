package org.neuro4j.workflow.hystrix;

public interface HystrixLifecycleListener {

    void onConstruct();

    void onCommandStart();

    void onCommandComplete();
}
