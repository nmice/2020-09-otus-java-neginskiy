package ru.otus.springMvc.basicDbSetup;

public interface MigrationsExecutor {
    void cleanDb();

    void executeMigrations();
}
