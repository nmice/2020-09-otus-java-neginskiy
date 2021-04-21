package ru.otus.springMvc.flyway;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
