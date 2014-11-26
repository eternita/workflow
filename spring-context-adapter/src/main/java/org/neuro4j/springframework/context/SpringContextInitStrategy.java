package org.neuro4j.springframework.context;

import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

/**
 * This class uses Spring's beanFactory to initialize Custom Blocks.
 *  Custom block should be marked as @Component (org.springframework.stereotype.Component)
 * If class does not have this annotation it will be loaded as simple class.
 */
public class SpringContextInitStrategy implements CustomBlockInitStrategy {

	private BeanFactory beanFactory;

	private final CustomBlockInitStrategy defaultStrategy;

	public SpringContextInitStrategy(BeanFactory beanFactory) {
		this();
		this.beanFactory = beanFactory;
	}

	public SpringContextInitStrategy() {
		super();
		this.defaultStrategy = new DefaultCustomBlockInitStrategy();
	}
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}



	/**
	 * 
	 */
	public CustomBlock loadCustomBlock(String className)
			throws FlowInitializationException {
		try {

			Class<? extends CustomBlock> clazz = (Class<? extends CustomBlock>) Class.forName(className);
			if (null != clazz) {
				Component component = clazz.getAnnotation(org.springframework.stereotype.Component.class);
				if (component != null) {
					CustomBlock customBlock = beanFactory.getBean(clazz);
					customBlock.init();
					return customBlock;
				}
			}

		} catch (ClassNotFoundException e) {
			Logger.error(this, e);
			throw new FlowInitializationException(e);
		}

		return this.defaultStrategy.loadCustomBlock(className);

	}
	
	

}
