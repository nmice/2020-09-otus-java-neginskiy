package ru.otus.webserver;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.webserver.dao.ClientDao;
import ru.otus.webserver.dao.ClientDaoHibernate;
import ru.otus.webserver.flyway.MigrationsExecutorFlyway;
import ru.otus.webserver.hibernate.HibernateUtils;
import ru.otus.webserver.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.webserver.model.AddressDataSet;
import ru.otus.webserver.model.Client;
import ru.otus.webserver.model.PhoneDataSet;
import ru.otus.webserver.service.DBServiceClient;
import ru.otus.webserver.service.DbServiceClientImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Neginskiy M.B. 07.04.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Веб сервер
 * Цель:
 * Научиться создавать серверный и пользовательский http-интерфейсы. Научиться встраивать web-сервер
 * в уже готовое приложение.
 * <p>
 * Встроить веб-сервер в приложение из ДЗ про Hibernate ORM (или в пример из вебинара встроить ДЗ про Hibernate :)).
 * Сделать стартовую страницу, на которой админ должен аутентифицироваться. Сделать админскую страницу для работы
 * с пользователями. На этой странице должны быть доступны следующие функции:
 * - создать пользователя
 * - получить список пользователей
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

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).cleanDb();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class,
                AddressDataSet.class, PhoneDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

// Работа с клиентами
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(clientDao);

        long clientId = dbServiceClient.saveClient(new Client("Вася", 23,
                new AddressDataSet("Marx st."),
                Collections.singletonList("84950010203")));
        Optional<Client> clientOptional = dbServiceClient.getClientById(clientId);
        clientOptional.ifPresentOrElse(
                client -> log.info("created client, name:{}", client.getName()),
                () -> log.info("client was not created")
        );
        System.err.println("Client1 phones: " + clientOptional.get().getPhones());

        long clientId2 = dbServiceClient.saveClient(new Client("Коля", 25,
                new AddressDataSet("Lenina st."),
                Arrays.asList("84956666666", "89850000000")));
        Optional<Client> clientOptional2 = dbServiceClient.getClientById(clientId2);
        clientOptional2.ifPresentOrElse(
                client -> log.info("created client, name:{}", client.getName()),
                () -> log.info("client was not created")
        );
        System.err.println("Client1 phones: " + clientOptional2.get().getPhones());
    }
}
