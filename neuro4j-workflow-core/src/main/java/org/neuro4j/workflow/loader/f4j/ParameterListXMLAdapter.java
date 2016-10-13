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

package org.neuro4j.workflow.loader.f4j;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ParameterListXMLAdapter extends XmlAdapter<ParameterXML[], List<ParameterXML>> {

    public List<ParameterXML> unmarshal(ParameterXML[] array) {
        List<ParameterXML> propList = new ArrayList<ParameterXML>();
        for (ParameterXML p : array)
            propList.add(p);
        return propList;
    }

    @Override
    public ParameterXML[] marshal(List<ParameterXML> value)
            throws Exception {
        return value.toArray(new ParameterXML[value.size()]);
    }
}