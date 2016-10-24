package org.neuro4j.workflow.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.core.SystemOutBlock;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.node.CustomBlockLoader;
import org.neuro4j.workflow.node.CustomNode;

public class CacheTest {

	@Before
	public void setUp() {

	}

	@Test
	public void testCreateEngineWithMapCache() throws FlowExecutionException {

		Map<String, AtomicInteger> counters = new HashMap<>();
		
		CustomBlockInitStrategy customBlockInitStrategy = new CustomBlockInitStrategy() {
			
		private final DefaultCustomBlockInitStrategy defaultInitStrategy = new DefaultCustomBlockInitStrategy();
			
			@Override
			public ActionBlock loadCustomBlock(String className) throws FlowExecutionException {

				AtomicInteger counter = counters.get(className);
				if (counter == null){
					counter = new AtomicInteger(0);
					counters.put(className, counter);
				}
				counter.getAndIncrement();
				
				return defaultInitStrategy.loadCustomBlock(className);
			}
		};
		
		
		String name = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		
		CustomNode  systemOutBlock = new CustomNode("org.neuro4j.workflow.core.SystemOutBlock", name, uuid);

		ConcurrentHashMap<String, ActionBlock> cache = new ConcurrentHashMap<>();
		
		CustomBlockLoader loader = new CustomBlockLoader(customBlockInitStrategy, cache);
		
		ActionBlock  sOut = loader.lookupBlock(systemOutBlock);
		assertNotNull(sOut);
		assertThat(sOut, IsInstanceOf.instanceOf(SystemOutBlock.class));
		assertThat(cache, IsMapContaining.hasKey(Is.is("org.neuro4j.workflow.core.SystemOutBlock")));
		assertThat(cache.values(), IsCollectionWithSize.hasSize(1));
		assertEquals(counters.get("org.neuro4j.workflow.core.SystemOutBlock").get(), 1);
		
		String name1 = UUID.randomUUID().toString();
		String uuid1 = UUID.randomUUID().toString();
		
		CustomNode  systemOutBlock2 = new CustomNode("org.neuro4j.workflow.core.SystemOutBlock", name1, uuid1);

		
		ActionBlock sOut1 = loader.lookupBlock(systemOutBlock2);
		assertNotNull(sOut1);
		assertThat(sOut1, IsInstanceOf.instanceOf(SystemOutBlock.class));
		assertThat(cache.values(), IsCollectionWithSize.hasSize(1));
		assertEquals(counters.get("org.neuro4j.workflow.core.SystemOutBlock").get(), 1);

	
	}

}
