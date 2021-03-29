package ru.otus.jpql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jpql.dao.ClientDao;
import ru.otus.jpql.dao.ClientDaoHibernate;
import ru.otus.jpql.flyway.MigrationsExecutorFlyway;
import ru.otus.jpql.hibernate.HibernateUtils;
import ru.otus.jpql.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.jpql.model.AddressDataSet;
import ru.otus.jpql.model.Client;
import ru.otus.jpql.model.PhoneDataSet;
import ru.otus.jpql.service.DBServiceClient;
import ru.otus.jpql.service.DbServiceClientImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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
