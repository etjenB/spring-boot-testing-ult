package org.etjen.spring_boot_testing_ult.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractContainerBase {
    static final PostgreSQLContainer postgresSqlContainer;
    static {
        postgresSqlContainer = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("tests")
                .withUsername("postgres")
                .withPassword("0000");
        postgresSqlContainer.start();
    }
}
