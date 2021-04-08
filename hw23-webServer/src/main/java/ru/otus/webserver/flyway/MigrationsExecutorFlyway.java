package ru.otus.webserver.flyway;

import org.flywaydb.core.Flyway;

public class MigrationsExecutorFlyway {

    private final Flyway flyway;

    public MigrationsExecutorFlyway(String dbUrl, String dbUserName, String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .load();
    }

    public void cleanDb() {
        flyway.clean();
    }
}
