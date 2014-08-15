package org.springframework.samples.mvc.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public String serve() {
		System.out.println("Hello from service");
		return "Hello";
	}

}
