/*
 * Copyright (c) 2013-2014, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.neuro4j.workflow.guice;


import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.CustomBlock;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;



/**
 * This class allows to use Google Guice library during initialization of all custom blocks.
 *
 */
public class GuiceCustomBlockInitStrategy implements CustomBlockInitStrategy {

    /**
     *  Guice's injector.
     */
    private final Injector injector;
    
    /**
     * The constructor.
     * @param modules Guice's modules which will be used by injector.
     */
    public GuiceCustomBlockInitStrategy(Iterable<? extends Module> modules) {
        super();
        injector = Guice.createInjector(modules);
    }

    
    /* (non-Javadoc)
     * @see org.neuro4j.workflow.loader.CustomBlockInitStrategy#loadCustomBlock(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public CustomBlock loadCustomBlock(String className) throws FlowInitializationException {
        try {

            Class<? extends CustomBlock> clazz = (Class<? extends CustomBlock>) Class.forName(className);
            if (null != clazz)
            {
                CustomBlock customBlock =  injector.getInstance(clazz);
                customBlock.init();
                return customBlock;
            }

        } catch (ClassNotFoundException e) {
            Logger.error(this, e);
            throw new FlowInitializationException(e);
        }
        throw new FlowInitializationException("CustomBlock: " + className + " not found");
    }

}
