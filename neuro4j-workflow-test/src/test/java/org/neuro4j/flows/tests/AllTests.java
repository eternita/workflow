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

package org.neuro4j.flows.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.neuro4j.flows.custom.InputParametersTestCase;
import org.neuro4j.flows.network.NetworkTestCase;
import org.neuro4j.flows.nodes.callnode.CallNodeTestCase;
import org.neuro4j.flows.nodes.decisionnode.DecisionNodeTestCase;
import org.neuro4j.flows.nodes.loopnode.LoopNodeTestCase;
import org.neuro4j.flows.nodes.mappernode.MapperNodeTestCase;
import org.neuro4j.flows.nodes.switchnode.SwitchNodeTestCase;
import org.neuro4j.flows.nodes.viewnode.SetViewTemplateNodeTestCase;

@RunWith(Suite.class)
@SuiteClasses({ DecisionNodeTestCase.class, SwitchNodeTestCase.class,
        LoopNodeTestCase.class, MapperNodeTestCase.class, CallNodeTestCase.class, SetViewTemplateNodeTestCase.class, InputParametersTestCase.class, NetworkTestCase.class })
public class AllTests {

}
