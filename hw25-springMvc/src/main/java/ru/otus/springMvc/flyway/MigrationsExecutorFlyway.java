package ru.otus.springMvc.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:flyway.properties")
@Component
public class MigrationsExecutorFlyway implements MigrationsExecutor {

    private final Flyway flyway;

    public MigrationsExecutorFlyway(@Value("${flyway.url}") String dbUrl,
                                    @Value("${flyway.username}") String dbUserName,
                                    @Value("${flyway.password}") String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/migration")
                .load();
        cleanDb();
        executeMigrations();
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
