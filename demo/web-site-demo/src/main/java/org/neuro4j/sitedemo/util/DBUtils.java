package org.neuro4j.sitedemo.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBUtils {
	
	private static ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/data-source.xml");

	public static Connection getConnection() throws SQLException
	{
		DataSource ds = (DataSource) context.getBean("dataSource");
		
		return ds.getConnection();
	}
}
