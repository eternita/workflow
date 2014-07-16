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

package org.neuro4j.workflow.loader;

import java.util.Hashtable;

import org.neuro4j.workflow.ExecutableEntityFactory;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.node.CustomBlock;
import org.neuro4j.workflow.node.CustomNode;

/**
 * Loads and initializes custom (user's defined) blocks.
 * 
 */
public class LogicBlockLoader {

    private static LogicBlockLoader INSTANCE = new LogicBlockLoader();

    private Hashtable<String, CustomBlock> cache = new Hashtable<String, CustomBlock>();

    /**
     * Constructor.
     */
    private LogicBlockLoader() {
        super();
    }

    public static LogicBlockLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Lookups customBlock by executable class.
     * If block does not exist in cache - creates instance and init. it.
     * 
     * @param entity
     * @return
     * @throws FlowInitializationException
     */
    public CustomBlock lookupBlock(CustomNode entity) throws FlowInitializationException
    {
        CustomBlock block = cache.get(entity.getExecutableClass());

        if (block == null)
        {
            block = (CustomBlock) ExecutableEntityFactory.getActionEntity(entity);
            block.init();
            cache.put(entity.getExecutableClass(), block);
        }

        return block;
    }
}
