package org.neuro4j.flows.custom;

import java.util.ArrayList;
import java.util.List;

public class TestBean {
	
	private boolean booleanVar = false;
	String stringVar = "Hello";
	Integer integerVar = new Integer(123);
	List<String> list = new ArrayList<String>();
	
	TestBean bean;

	public boolean isBooleanVar() {
		return booleanVar;
	}

	public void setBooleanVar(boolean booleanVar) {
		this.booleanVar = booleanVar;
	}

	public String getStringVar() {
		return stringVar;
	}

	public void setStringVar(String stringVar) {
		this.stringVar = stringVar;
	}

	public Integer getIntegerVar() {
		return integerVar;
	}

	public void setIntegerVar(Integer integerVar) {
		this.integerVar = integerVar;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public TestBean getBean() {
		return bean;
	}

	public void setBean(TestBean bean1) {
		this.bean = bean1;
	}
	
	

}
