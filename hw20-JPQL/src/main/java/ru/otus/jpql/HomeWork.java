package ru.otus.jpql;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jpql.dao.ClientDao;
import ru.otus.jpql.flyway.MigrationsExecutorFlyway;
import ru.otus.jpql.hibernate.HibernateUtils;
import ru.otus.jpql.dao.ClientDaoHibernate;
import ru.otus.jpql.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.jpql.model.Account;
import ru.otus.jpql.model.AddressDataSet;
import ru.otus.jpql.model.Client;
import ru.otus.jpql.model.PhoneDataSet;
import ru.otus.jpql.service.DBServiceClient;
import ru.otus.jpql.service.DbServiceAccountImpl;
import ru.otus.jpql.service.DbServiceClientImpl;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Neginskiy M.B. 29.03.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Использование Hibernate
 * Цель:
 * На практике освоить основы Hibernate. Понять как аннотации-hibernate влияют на формирование sql-запросов.
 * <p>
 * Работа должна использовать базу данных в docker-контейнере .
 * <p>
 * Возьмите за основу предыдущее ДЗ (Самодельный ORM),
 * используйте предложенный на вебинаре api (пакет ru.otus.core, вебинар про Hibernate).
 * и реализуйте функционал сохранения и чтения объекта Client через Hibernate.
 * (Рефлексия больше не нужна) Конфигурация Hibernate должна быть вынесена в файл.
 * <p>
 * Добавьте в Client поля: адрес (OneToOne) class AddressDataSet { private String street; }
 * и телефон (OneToMany) class PhoneDataSet { private String number; }
 * <p>
 * Разметьте классы таким образом, чтобы при сохранении/чтении объекта Client каскадно сохранялись/читались
 * вложенные объекты.
 * <p>
 * ВАЖНО.
 * <p>
 * Hibernate должен создать только три таблицы: для телефонов, адресов и клиентов.
 * При сохранении нового объекта не должно быть update-ов. Посмотрите в логи и проверьте,
 * что эти два требования выполняются.
 */
public class HomeWork {
    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
// Общая часть
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(clientDao);

// Работа с клиентами
        long id = dbServiceClient.saveClient(new Client(0, "Вася", 23,
                new AddressDataSet(),
                Arrays.asList(new PhoneDataSet())));
        Optional<Client> mayBeCreatedClient = dbServiceClient.getClientById(id);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao, sessionManager);

        var clientId = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 17, address, phones));

        Optional<Client> clientOptional = dbServiceClient.getClientById(clientId);
        clientOptional.ifPresentOrElse(
                client -> log.info("created client, name:{}", client.getName()),
                () -> log.info("client was not created")
        );

        dbServiceClient.saveClient(new Client(clientId, "dbServiceClientUpdated", 25, address, phones));

        clientOptional = dbServiceClient.getClientById(clientId);
        clientOptional.ifPresentOrElse(
                client -> log.info("updated client, name:{}", client.getName()),
                () -> log.info("client was not updated")
        );

// Работа со счетом
        DbExecutorImpl<Account> dbAccountExecutor = new DbExecutorImpl<>();
        EntityClassMetaData<Account> accountClassMetaData = new EntityClassMetaDataImpl<>(Account.class);
        EntitySQLMetaData accountSQLMetaData = EntitySQLMetaDataImpl.of(accountClassMetaData);
        JdbcMapper<Account> accountDao = new JdbcMapperImpl<>(sessionManager, dbAccountExecutor, accountSQLMetaData,
                accountClassMetaData);

        var dbServiceAccount = new DbServiceAccountImpl(accountDao, sessionManager);

        String accountId = UUID.randomUUID().toString();

        dbServiceAccount.saveAccount(new Account(accountId, "dbServiceAccountType", 17.78));

        Optional<Account> accountOptional = dbServiceAccount.getAccountById(accountId);
        accountOptional.ifPresentOrElse(
                account -> log.info("created account, type:{}", account.getType()),
                () -> log.info("account was not created")
        );

        dbServiceAccount.saveAccount(new Account(accountId, "dbServiceAccountTypeUpdated", 12.39));

        accountOptional = dbServiceAccount.getAccountById(accountId);
        accountOptional.ifPresentOrElse(
                account -> log.info("updated account, type:{}", account.getType()),
                () -> log.info("account was not updated")
        );
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
