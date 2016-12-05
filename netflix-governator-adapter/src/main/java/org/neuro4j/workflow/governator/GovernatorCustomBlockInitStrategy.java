/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.neuro4j.workflow.governator;


import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.node.CustomBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Injector;
import com.netflix.governator.lifecycle.LifecycleManager;



/**
 * This class allows to use Netflix Governator library during initialization of all custom blocks.
 *
 */
public class GovernatorCustomBlockInitStrategy implements CustomBlockInitStrategy {

	private static final Logger Logger = LoggerFactory.getLogger(GovernatorCustomBlockInitStrategy.class);

    /**
     *  Guice's injector.
     */
    private final Injector injector;
    

    public GovernatorCustomBlockInitStrategy(Injector injector) throws Exception {
        super();
        this.injector = injector;
        LifecycleManager manager = injector.getInstance(LifecycleManager.class);
        manager.start();
    }

    
    @SuppressWarnings("unchecked")
    public ActionBlock loadCustomBlock(String className) throws FlowExecutionException {
        try {

            Class<? extends ActionBlock> clazz = (Class<? extends CustomBlock>) Class.forName(className);
            if (null != clazz)
            {
            	ActionBlock customBlock =  injector.getInstance(clazz);
                return customBlock;
            }

        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            throw new FlowExecutionException(e);
        }
        throw new FlowExecutionException("CustomBlock: " + className + " not found");
    }

}
