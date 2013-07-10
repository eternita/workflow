package org.neuro4j.flows.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.neuro4j.flows.custom.InputParametersTestCase;
import org.neuro4j.flows.nodes.callnode.CallNodeTestCase;
import org.neuro4j.flows.nodes.decisionnode.DecisionNodeTestCase;
import org.neuro4j.flows.nodes.loopnode.LoopNodeTestCase;
import org.neuro4j.flows.nodes.mappernode.MapperNodeTestCase;
import org.neuro4j.flows.nodes.switchnode.SwitchNodeTestCase;
import org.neuro4j.flows.nodes.viewnode.SetViewTemplateNodeTestCase;

@RunWith(Suite.class)
@SuiteClasses({ DecisionNodeTestCase.class, SwitchNodeTestCase.class,
		LoopNodeTestCase.class, MapperNodeTestCase.class, CallNodeTestCase.class, SetViewTemplateNodeTestCase.class, InputParametersTestCase.class })
public class AllTests {

}
