package com.example.pgdemo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Log4j2
@SpringBootTest
class PostgresTest {

	private final JdbcTemplate template;

	PostgresTest(@Autowired JdbcTemplate template) {
		this.template = template;
	}

	@Test
	void contextLoads() {

		this.template
			.query("select name from customer", (resultSet, i) -> resultSet.getString("name"))
			.forEach(log::info);


	}

}
