/*
 * Copyright (c) 2013-2016, Neuro4j
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

package org.neuro4j.workflow.node;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.log.Logger;

/**
 * Loads and initializes custom (user's defined) blocks.
 * 
 */
public class CustomBlockLoader {


    private final DefaultCustomBlockInitStrategy defaultInitStrategy = new DefaultCustomBlockInitStrategy();
    
    final private CustomBlockInitStrategy customBlockInitStrategy;

    /**
     * Constructor.
     */
    public CustomBlockLoader(final CustomBlockInitStrategy customBlockInitStrategy) {
        super();
        this.customBlockInitStrategy = customBlockInitStrategy;
    }


    /**
     * Lookups customBlock by executable class.
     * If block does not exist in cache - creates instance and init. it.
     * 
     * @param entity
     * @return
     * @throws FlowInitializationException
     */
    ActionBlock lookupBlock(CustomNode entity) throws FlowInitializationException
    {

        long start = System.currentTimeMillis();
        
        if (entity.getExecutableClass() == null)
        {
            throw new FlowInitializationException("ExecutableClass not defined for CustomNode " + entity.getName());
        }
        
        ActionBlock block = null;
        
        if (customBlockInitStrategy == null)
        {
            block = defaultInitStrategy.loadCustomBlock(entity.getExecutableClass());

        } else {
            block = customBlockInitStrategy.loadCustomBlock(entity.getExecutableClass());
        }
        
        block.init();
        
        Logger.debug(this, "CustomBlock {} loaded and initialized in {} ms", entity.getExecutableClass(), System.currentTimeMillis() - start);
        
        return block;
    }

    
    
    Class<? extends ActionBlock> getCustomBlockClass(CustomNode entity) throws FlowInitializationException
    {
        return defaultInitStrategy.getCustomBlockClass(entity.getExecutableClass());
    }
    
}
