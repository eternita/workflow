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
package org.neuro4j.workflow.node;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.enums.ActionBlockCache;
import org.neuro4j.workflow.enums.CachedNode;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.utils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads and initializes custom (user's defined) blocks.
 * 
 */
public class CustomBlockLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomBlockLoader.class);


	private final ConcurrentHashMap<String, ActionBlock> cache;

    private final DefaultCustomBlockInitStrategy defaultInitStrategy = new DefaultCustomBlockInitStrategy();
    
    final private CustomBlockInitStrategy customBlockInitStrategy;

    public CustomBlockLoader(final CustomBlockInitStrategy customBlockInitStrategy, final ConcurrentHashMap<String, ActionBlock> cache) {
        super();
        this.customBlockInitStrategy = Optional.ofNullable(customBlockInitStrategy).orElse(defaultInitStrategy);
        this.cache = cache;
    }
    
    public CustomBlockLoader(final CustomBlockInitStrategy customBlockInitStrategy) {
        this(customBlockInitStrategy, new ConcurrentHashMap<>());

    }


    /**
     * Lookups customBlock by executable class.
     * If block does not exist in cache - creates instance and init. it.
     * 
     * @param entity custom node
     * @return object implemented ActionBlock
     * @throws FlowExecutionException in case of error
     */
    
	public ActionBlock lookupBlock(CustomNode entity) throws FlowExecutionException {

		Validation.requireNonNull(entity.getExecutableClass(), 
				                  () -> new FlowExecutionException("ExecutableClass not defined for CustomNode " + entity.getName()));

		long start = System.currentTimeMillis();
		
		ActionBlockCache cacheType =  Optional.ofNullable(getCustomBlockClass(entity).getAnnotation(CachedNode.class))
				                              .map(n -> n.type())
				                              .orElseGet(() -> ActionBlockCache.NONE);		
		ActionBlock block = null;

		switch (cacheType) {
		case SINGLETON:
			block = cache.get(entity.getExecutableClass());
			if (block == null){
				block = loadCustomNode(entity);
				cache.put(entity.getExecutableClass(), block);	
			}
			
			break;

		case NODE:
			block = cache.get(entity.getUuid());
			if (block == null){
				block = loadCustomNode(entity);
				cache.put(entity.getUuid(), block);
			}
			break;

		case NONE:
			block = loadCustomNode(entity);
			break;
			
		default:
			throw new FlowExecutionException("Unknow cache type: " + cacheType);
		}


		logger.debug("CustomBlock {} loaded and initialized in {} ms", entity.getExecutableClass(),
				System.currentTimeMillis() - start);

		return block;
	}


	private ActionBlock loadCustomNode(CustomNode entity) throws FlowExecutionException {
		ActionBlock block = customBlockInitStrategy.loadCustomBlock(entity.getExecutableClass());
		block.init();
		return block;
	}
    
    
	Class<? extends ActionBlock> getCustomBlockClass(CustomNode entity) throws FlowExecutionException {
		return defaultInitStrategy.getCustomBlockClass(entity.getExecutableClass());
	}

}
