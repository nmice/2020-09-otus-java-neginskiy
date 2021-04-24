package ru.otus.springMvc.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@PropertySource("flyway.properties")
@Component
public class MigrationsExecutorFlyway implements MigrationsExecutor {

    private final Flyway flyway;

    public MigrationsExecutorFlyway(@Value("jdbc:postgresql://localhost:5432/hwDB") String dbUrl,
                                    @Value("usr") String dbUserName,
                                    @Value("pwd") String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/db/migration")
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
