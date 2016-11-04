package org.neuro4j.workflow.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.UUIDMgr;
import org.neuro4j.workflow.cache.ConcurrentMapWorkflowCache;
import org.neuro4j.workflow.cache.EmptyWorkflowCache;
import org.neuro4j.workflow.core.CustomBlockEmptyCache;
import org.neuro4j.workflow.core.CustomBlockNodeCached;
import org.neuro4j.workflow.core.CustomBlockNoneCached;
import org.neuro4j.workflow.core.SystemOutBlock;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.neuro4j.workflow.loader.WorkflowLoader;
import org.neuro4j.workflow.node.CustomBlockLoader;
import org.neuro4j.workflow.node.CustomNode;

public class CacheTest {

	
	@Mock
	WorkflowLoader workflowLoader;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateEngineWithMapCache() throws FlowExecutionException {

		Map<String, AtomicInteger> loadCounters = new HashMap<>();
		
		CustomBlockInitStrategy customBlockInitStrategy = new CustomBlockInitStrategy() {
			
		private final DefaultCustomBlockInitStrategy defaultInitStrategy = new DefaultCustomBlockInitStrategy();
			
			@Override
			public ActionBlock loadCustomBlock(String className) throws FlowExecutionException {

				AtomicInteger counter = loadCounters.get(className);
				if (counter == null){
					counter = new AtomicInteger(0);
					loadCounters.put(className, counter);
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
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.SystemOutBlock").get(), 1);
		
		String name1 = UUIDMgr.getInstance().createUUIDString();
		String uuid1 = UUIDMgr.getInstance().createUUIDString();
		
		CustomNode  systemOutBlock2 = new CustomNode("org.neuro4j.workflow.core.SystemOutBlock", name1, uuid1);

		
		ActionBlock sOut1 = loader.lookupBlock(systemOutBlock2);
		assertNotNull(sOut1);
		assertThat(sOut1, IsInstanceOf.instanceOf(SystemOutBlock.class));
		assertThat(cache.values(), IsCollectionWithSize.hasSize(1));
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.SystemOutBlock").get(), 1);

		
		// test  ActionBlockCache.NONE
		CustomNode  customBlockNoneCached1 = new CustomNode("org.neuro4j.workflow.core.CustomBlockNoneCached", UUID.randomUUID().toString(), UUID.randomUUID().toString());

		ActionBlock noneCached1 = loader.lookupBlock(customBlockNoneCached1);
		
		assertNotNull(noneCached1);
		assertThat(noneCached1, IsInstanceOf.instanceOf(CustomBlockNoneCached.class));
		
		// in cache just SystemOutBlock
		assertThat(cache.values(), IsCollectionWithSize.hasSize(1));
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.SystemOutBlock").get(), 1);
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockNoneCached").get(), 1);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockNoneCached"));
		
		//again for NONE cached
		
		CustomNode  customBlockNoneCached2 = new CustomNode("org.neuro4j.workflow.core.CustomBlockNoneCached", UUID.randomUUID().toString(), UUID.randomUUID().toString());

		ActionBlock noneCached2 = loader.lookupBlock(customBlockNoneCached2);
		
		assertNotNull(noneCached2);
		assertThat(noneCached1, IsInstanceOf.instanceOf(CustomBlockNoneCached.class));
		
		assertThat(cache.values(), IsCollectionWithSize.hasSize(1));
		// was loaded twice
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockNoneCached").get(), 2);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockNoneCached"));
		assertNotNull(cache.get("org.neuro4j.workflow.core.SystemOutBlock"));
		
		// now for NODE CACHED
		
		String nameForNodeCached = UUID.randomUUID().toString();
		String uuidForNodeCached = UUID.randomUUID().toString();
		
		CustomNode  customBlockNodeCached = new CustomNode("org.neuro4j.workflow.core.CustomBlockNodeCached", nameForNodeCached, uuidForNodeCached);

		ActionBlock nodeCached = loader.lookupBlock(customBlockNodeCached);
		
		assertNotNull(nodeCached);
		assertThat(nodeCached, IsInstanceOf.instanceOf(CustomBlockNodeCached.class));
		
		assertThat(cache.values(), IsCollectionWithSize.hasSize(2));
		
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockNodeCached").get(), 1);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockNodeCached"));
		// key is node uuid
		assertEquals(nodeCached, cache.get(uuidForNodeCached));
		
		// one more cached block
		String nameForNodeCached1 = UUID.randomUUID().toString();
		String uuidForNodeCached1 = UUID.randomUUID().toString();
		
		CustomNode  customBlockNodeCached1 = new CustomNode("org.neuro4j.workflow.core.CustomBlockNodeCached", nameForNodeCached1, uuidForNodeCached1);

		ActionBlock nodeCached1 = loader.lookupBlock(customBlockNodeCached1);
		
		assertNotNull(nodeCached1);
		assertThat(nodeCached1, IsInstanceOf.instanceOf(CustomBlockNodeCached.class));
		
		
		assertThat(cache.values(), IsCollectionWithSize.hasSize(3));
		
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockNodeCached").get(), 2);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockNodeCached"));
		// key is node uuid
		assertEquals(nodeCached1, cache.get(uuidForNodeCached1));
		
		
		nodeCached1 = loader.lookupBlock(customBlockNodeCached1);
		
		assertNotNull(nodeCached1);
		assertThat(nodeCached1, IsInstanceOf.instanceOf(CustomBlockNodeCached.class));
		
		
		assertThat(cache.values(), IsCollectionWithSize.hasSize(3));
		
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockNodeCached").get(), 2);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockNodeCached"));
		// key is node uuid
		assertEquals(nodeCached1, cache.get(uuidForNodeCached1));
		
        // test default cache
		String uuidForEmptyCache = UUID.randomUUID().toString();
		CustomNode  customBlockEmptyCache = new CustomNode("org.neuro4j.workflow.core.CustomBlockEmptyCache", UUID.randomUUID().toString(), uuidForEmptyCache);

		CustomBlockEmptyCache emptyCached = (CustomBlockEmptyCache) loader.lookupBlock(customBlockEmptyCache);
		
		assertNotNull(emptyCached);
		assertThat(emptyCached, IsInstanceOf.instanceOf(CustomBlockEmptyCache.class));
		
		assertEquals(1, emptyCached.getInitCounter().get());

		
		// should not change cache size - should act as NONE
		assertThat(cache.values(), IsCollectionWithSize.hasSize(3));
		
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockEmptyCache").get(), 1);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockEmptyCache"));
		assertNull(cache.get(uuidForEmptyCache));
		
		emptyCached = (CustomBlockEmptyCache) loader.lookupBlock(customBlockEmptyCache);
		assertEquals(1, emptyCached.getInitCounter().get());
		
		assertNotNull(emptyCached);
		assertThat(emptyCached, IsInstanceOf.instanceOf(CustomBlockEmptyCache.class));
		
		// should not change cache size - should act as NONE
		assertThat(cache.values(), IsCollectionWithSize.hasSize(3));
		
		assertEquals(loadCounters.get("org.neuro4j.workflow.core.CustomBlockEmptyCache").get(), 2);
		assertNull(cache.get("org.neuro4j.workflow.core.CustomBlockEmptyCache"));
		assertNull(cache.get(uuidForEmptyCache));
		
	}
	
	@Test
	public void testEmptyWorkflowCache() throws FlowExecutionException {
		EmptyWorkflowCache cache =  EmptyWorkflowCache.INSTANCE;
		
		String flowname = "someflow";
		
		cache.get(workflowLoader, flowname);
		verify(workflowLoader, times(1)).load(flowname);
		
		cache.clear(flowname);
		verifyZeroInteractions(workflowLoader);
		
		cache.clearAll();
		verifyZeroInteractions(workflowLoader);
	}
	
	@Test
	public void testConcurrentMapWorkflowCache() throws FlowExecutionException {
		ConcurrentMap<String, Workflow> map = new ConcurrentHashMap<>();
		ConcurrentMapWorkflowCache cache = new ConcurrentMapWorkflowCache(map);
		
		String flowname = "someflow";
		
		when(workflowLoader.load(flowname)).thenReturn(new Workflow("someName", "someId"));
		
		cache.get(workflowLoader, flowname);
		assertEquals(1, map.size());
		
		verify(workflowLoader, times(1)).load(flowname);

		
		cache.clear(flowname);
		assertEquals(0, map.size());
		cache.get(workflowLoader, flowname);
		assertEquals(1, map.size());
		
		cache.clearAll();
		assertEquals(0, map.size());
	}

}
