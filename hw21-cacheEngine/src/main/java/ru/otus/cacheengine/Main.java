package ru.otus.cacheengine;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cacheengine.cachehw.MyCache;
import ru.otus.cacheengine.dao.ClientDao;
import ru.otus.cacheengine.dao.ClientDaoHibernate;
import ru.otus.cacheengine.flyway.MigrationsExecutorFlyway;
import ru.otus.cacheengine.hibernate.HibernateUtils;
import ru.otus.cacheengine.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.cacheengine.model.AddressDataSet;
import ru.otus.cacheengine.model.Client;
import ru.otus.cacheengine.model.PhoneDataSet;
import ru.otus.cacheengine.service.DBServiceClient;
import ru.otus.cacheengine.service.DbServiceClientImpl;
import ru.otus.cacheengine.service.DbServiceClientWithCacheImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Neginskiy M.B. 04.04.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Свой cache engine
 * Цель:
 * Научится применять WeakHashMap, понять базовый принцип организации кеширования.
 * <p>
 * Закончите реализацию MyCache из вебинара. Используйте WeakHashMap для хранения значений.
 * <p>
 * Добавьте кэширование в DBService из задания про Hibernate ORM или "Самодельный ORM".
 * Для простоты скопируйте нужные классы в это ДЗ.
 * <p>
 * Убедитесь, что ваш кэш действительно работает быстрее СУБД и сбрасывается при недостатке памяти.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

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
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);

// Работа c НЕкэшированным сервисом
        DBServiceClient dbServiceClient = new DbServiceClientImpl(clientDao);
        long clientId = dbServiceClient.saveClient(new Client("Вася", 23,
                new AddressDataSet("Marx st."),
                Collections.singletonList("84950010203")));
        long start1 = System.currentTimeMillis();
        Optional<Client> clientOptional = dbServiceClient.getClientById(clientId);
        clientOptional.ifPresentOrElse(
                client -> log.info("created client, name:{}", client.getName()),
                () -> log.info("client was not created")
        );
        System.err.println(String.format("***GETTING CLIENT W/O CACHE*** : %s millis",
                System.currentTimeMillis() - start1));
        System.out.println("Client1 phones: " + clientOptional.get().getPhones());

// Работа c кэшированным сервисом
        DBServiceClient dbServiceClientWithCache = new DbServiceClientWithCacheImpl(clientDao, new MyCache<String, Client>());
        long clientId2 = dbServiceClientWithCache.saveClient(new Client("Коля", 25,
                new AddressDataSet("Lenina st."),
                Arrays.asList("84956666666", "89850000000")));
        long start2 = System.currentTimeMillis();
        Optional<Client> clientOptional2 = dbServiceClientWithCache.getClientById(clientId2);
        clientOptional2.ifPresentOrElse(
                client -> log.info("created client, name:{}", client.getName()),
                () -> log.info("client was not created")
        );
        System.err.println(String.format("***GETTING CLIENT WITH CACHE*** : %s millis",
                System.currentTimeMillis() - start2));
        System.out.println("Client2 phones: " + clientOptional2.get().getPhones());
    }
}
