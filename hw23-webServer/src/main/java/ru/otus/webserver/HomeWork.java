package ru.otus.webserver;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.webserver.dao.UserDaoHibernate;
import ru.otus.webserver.flyway.MigrationsExecutor;
import ru.otus.webserver.flyway.MigrationsExecutorFlyway;
import ru.otus.webserver.hibernate.HibernateUtils;
import ru.otus.webserver.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.webserver.model.User;
import ru.otus.webserver.processor.TemplateProcessor;
import ru.otus.webserver.processor.TemplateProcessorImpl;
import ru.otus.webserver.server.UsersWebServer;
import ru.otus.webserver.server.UsersWebServerWithSecurity;
import ru.otus.webserver.service.auth.UserAuthService;
import ru.otus.webserver.service.auth.UserAuthServiceImpl;
import ru.otus.webserver.service.db.DBServiceUser;
import ru.otus.webserver.service.db.DbServiceUserImpl;
import ru.otus.webserver.helpers.DbHelper;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/

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
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.cleanDb();
        migrationsExecutor.executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        DBServiceUser userService = new DbServiceUserImpl(new UserDaoHibernate(sessionManager));
        DbHelper.fillDb(userService);
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService userAuthService = new UserAuthServiceImpl(userService);

        UsersWebServer usersWebServer = new UsersWebServerWithSecurity(WEB_SERVER_PORT,
                userService, templateProcessor, userAuthService);

        usersWebServer.start();
        usersWebServer.join();
    }
}
