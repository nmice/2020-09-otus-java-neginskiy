package ru.otus.customorm;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.customorm.datasource.HwDataSource;
import ru.otus.customorm.jdbc.DbExecutorImpl;
import ru.otus.customorm.jdbc.mapper.*;
import ru.otus.customorm.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.customorm.model.Account;
import ru.otus.customorm.model.Client;
import ru.otus.customorm.service.DbServiceAccountImpl;
import ru.otus.customorm.service.DbServiceClientImpl;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

/**
 * Neginskiy M.B. 14.03.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Самодельный ORM
 * Цель: Научиться работать с jdbc.
 * На практике освоить многоуровневую архитектуру приложения.
 * Работа должна использовать базу данных в docker-контейнере .
 * <p>
 * Создайте в базе таблицу Client с полями:
 * <p>
 * • id целое число
 * • name строка
 * • age целое число
 * <p>
 * Создайте свою аннотацию @Id
 * <p>
 * Создайте класс Client (с полями, которые соответствуют таблице, поле id отметьте аннотацией).
 * <p>
 * Реализуйте интерфейс JdbcMapper<T>, который умеет работать с классами, в которых есть поле с аннотацией @Id. ?JdbcMapper<T> должен сохранять объект в базу и читать объект из базы.
 * Для этого надо реализовать оставшиеся интерфейсы из пакета mapper.
 * Таким образом, получится надстройка над DbExecutor<T>, которая по заданному классу умеет генерировать sql-запросы.
 * А DbExecutor<T> должен выполнять сгенерированные запросы.
 * <p>
 * Имя таблицы должно соответствовать имени класса, а поля класса - это колонки в таблице.
 * Проверьте его работу на классе Client.
 * <p>
 * Создайте еще одну таблицу Account:
 * • no строка
 * • type строка
 * • rest число с плавающей точкой
 * <p>
 * Создайте для этой таблицы класс Account и проверьте работу JdbcMapper на этом классе.
 */
public class HomeWork {

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new HwDataSource();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с клиентами
        DbExecutorImpl<Client> dbClientExecutor = new DbExecutorImpl<>();
        EntityClassMetaData<Client> clientClassMetaData = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData clientSQLMetaData = EntitySQLMetaDataImpl.of(clientClassMetaData);
        JdbcMapper<Client> clientDao = new JdbcMapperImpl<>(sessionManager, dbClientExecutor, clientSQLMetaData,
                clientClassMetaData);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao, sessionManager);

        var clientId = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 17));

        Optional<Client> clientOptional = dbServiceClient.getClientById(clientId);
        clientOptional.ifPresentOrElse(
                client -> log.info("created client, name:{}", client.getName()),
                () -> log.info("client was not created")
        );

        dbServiceClient.saveClient(new Client(clientId, "dbServiceClientUpdated", 25));

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
