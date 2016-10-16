package org.neuro4j.springframework.context;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.log.Logger;
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
	public ActionBlock loadCustomBlock(String className)
			throws FlowInitializationException {
		try {
			
			Class<?> clazz=	getClass().getClassLoader().loadClass(className);
			if (null != clazz) {
				if (ActionBlock.class.isAssignableFrom(clazz)){
					Component component = clazz.getAnnotation(org.springframework.stereotype.Component.class);
					if (component != null) {
						ActionBlock customBlock = (ActionBlock) beanFactory.getBean(clazz);
						return customBlock;
					}					
				} else {
					throw new FlowInitializationException("Class " + className + " does not implement org.neuro4j.workflow.ActionBlock");
				}

			}

		} catch (ClassNotFoundException e) {
			Logger.error(this, e);
			throw new FlowInitializationException(e);
		}

		return this.defaultStrategy.loadCustomBlock(className);

	}
	
	

}
