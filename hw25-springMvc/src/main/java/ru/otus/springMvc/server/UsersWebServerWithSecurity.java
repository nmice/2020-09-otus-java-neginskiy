package ru.otus.springMvc.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.webserver.helpers.FileSystemHelper;
import ru.otus.webserver.processor.TemplateProcessor;
import ru.otus.webserver.service.auth.UserAuthService;
import ru.otus.webserver.service.db.DBServiceUser;
import ru.otus.webserver.servlet.AuthorizationFilter;
import ru.otus.webserver.servlet.LoginServlet;
import ru.otus.webserver.servlet.UsersApiServlet;
import ru.otus.webserver.servlet.UsersServlet;

import java.util.Arrays;

public class UsersWebServerWithSecurity implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;
    private final Server server;
    private final UserAuthService userAuthService;
    private final Gson gson;

    public UsersWebServerWithSecurity(int port,
                                      DBServiceUser userService,
                                      TemplateProcessor templateProcessor,
                                      UserAuthService userAuthService,
                                      Gson gson) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.userAuthService = userAuthService;
        this.gson = gson;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*"));

        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, userAuthService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, userService)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(userService, gson)), "/api/user/*");
        return servletContextHandler;
    }
}
