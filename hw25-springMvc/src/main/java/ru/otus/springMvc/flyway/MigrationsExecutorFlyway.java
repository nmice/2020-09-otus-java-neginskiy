package ru.otus.springMvc.flyway;

import org.flywaydb.core.Flyway;

public class MigrationsExecutorFlyway implements MigrationsExecutor {

    private final Flyway flyway;

    public MigrationsExecutorFlyway(String dbUrl, String dbUserName, String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .load();
    }

    @Override
    public void cleanDb() {
        flyway.clean();
    }

    @Override
    public void executeMigrations() {
        flyway.migrate();
    }
}
