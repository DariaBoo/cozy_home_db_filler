package com.cozyhome.onlineshop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SqlExecuter {
private final DataSource dataSource;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String userName;
	@Value("${spring.datasource.password}")
	private String password;

	public void executeSqlFromFile(String fileName) {
		try (Connection connection = dataSource.getConnection();
	             Statement statement = connection.createStatement();
	             BufferedReader reader = new BufferedReader(
	                     new InputStreamReader(getClass().getResourceAsStream(fileName)))) {

	            String line;
	            StringBuilder query = new StringBuilder();

	            while ((line = reader.readLine()) != null) {
	                query.append(line);
	                if (line.endsWith(";")) {
	                    statement.execute(query.toString());
	                    query.setLength(0);
	                }
	            }
	            log.info("FILE [" + fileName + "] EXECUTED.");
	        } catch (SQLException | IOException e) {
	            e.printStackTrace();
	        }
	}

}
