package org.neuro4j.springframework.timer;

import java.util.HashMap;

import org.neuro4j.workflow.common.TriggerBlock;
import org.springframework.stereotype.Component;


@Component
public class Timer extends TriggerBlock
{

	public void execute() {		
		executeFlow(new HashMap<String, Object>());
	}
}
