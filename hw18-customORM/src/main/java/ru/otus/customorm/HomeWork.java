package ru.otus.customorm;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.customorm.core.dao.ClientDao;
import ru.otus.customorm.core.model.Client;
import ru.otus.customorm.core.service.DbServiceClientImpl;
import ru.otus.customorm.demo.DataSourceDemo;
import ru.otus.customorm.jdbc.DbExecutorImpl;
import ru.otus.customorm.jdbc.mapper.JdbcMapper;
import ru.otus.customorm.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceDemo();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Client> jdbcMapperClient = null; //
        ClientDao clientDao = null; // = new UserDaoJdbcMapper(sessionManager, dbExecutor);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client(0, "dbServiceClient", age));
        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name:{}", client.getName()),
                () -> logger.info("client was not created")
        );
// Работа со счетом


    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
