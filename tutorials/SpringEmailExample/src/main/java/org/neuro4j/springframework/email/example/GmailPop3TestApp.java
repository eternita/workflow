package org.neuro4j.springframework.email.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class GmailPop3TestApp {

	public static void main (String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("/META-INF/spring/integration/gmail-pop3-config.xml");
	}
}
