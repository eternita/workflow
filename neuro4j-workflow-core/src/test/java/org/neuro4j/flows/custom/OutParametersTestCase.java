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

package org.neuro4j.flows.custom;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;
import org.neuro4j.workflow.ExecutionResult;

public class OutParametersTestCase extends BaseFlowTestCase {



    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMappingOutParameter() {

        Map<String, Object> params = new HashMap<String, Object>();
        String int1 = "123";
        params.put("in1", int1);
        
        ExecutionResult result = executeFlowAndReturnResult("org.neuro4j.flows.custom.FlowWithParameters-StartNode6", params);
        String out2 = (String)result.getFlowContext().get("out2");
        Assert.assertEquals(int1, out2);

    }

  
}
