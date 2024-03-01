package com.avalialivros.m3s04.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@ContextConfiguration(initializers = TestContainersDatabaseConfiguration.Initializer.class)
public class TestContainersDatabaseConfiguration {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1");

        private static void initContainer() {
            postgreSQLContainer
                    .withInitScript("scripts/integrationtestinitscript.sql")
                    .start();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username", postgreSQLContainer.getUsername(),
                    "spring.datasource.password", postgreSQLContainer.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            initContainer();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainersProperties =
                    new MapPropertySource("testContainers", createConnectionConfiguration());
            environment.getPropertySources().addFirst(testContainersProperties);
        }
    }
}
