package org.neuro4j.springframework.timer.example;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	public static void main(String[] args) {		
		 AbstractApplicationContext context = new ClassPathXmlApplicationContext("quartz-context.xml");

	}
}
