package org.neuro4j.workflow.springmvc;

import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.context.SpringContextInitStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * This class initializes CustomBlockInitStrategy and should be registered as bean.
 * <beans:bean  class="org.neuro4j.workflow.springmvc.WorkflowWebApplicationListener" />
 *
 */
public class WorkflowWebApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        WorkflowEngine.setCustomBlockInitStrategy(new SpringContextInitStrategy(applicationContext));
    }
}
