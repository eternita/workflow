package org.neuro4j.workflow.tutorial.mvc.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	public String serve(String fruit) {
		return "I like " + fruit + "!";
	}

}
