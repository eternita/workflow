package org.neuro4j.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.neuro4j"})
public class SampleApplication  {

	public static void main(String[] args) {
		   SpringApplication.run(SampleApplication.class, args);
	}

}