package ru.otus.springMvc.basicDbConfig;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
