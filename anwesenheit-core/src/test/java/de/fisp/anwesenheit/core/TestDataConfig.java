package de.fisp.anwesenheit.core;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataConfig {
	public TestDataFactory testDataFactory() {
		return new TestDataFactory();
	}
}
