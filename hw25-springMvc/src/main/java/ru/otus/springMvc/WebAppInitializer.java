package ru.otus.springMvc;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Neginskiy M.B. 21.04.2021
 * <p>
 * ДОМАШНЕЕ ЗАДАНИЕ
 * Веб-приложение на Spring MVC
 * Цель:
 * Научиться создавать war-пакеты и запускать их в TomCat. Научиться пользоваться Thymeleaf.
 * <p>
 * Собрать war для приложения из ДЗ про Web Server.
 * Создавать основные классы приложения, как Spring beans (Кэш, Dao, DBService).
 * Настройку зависимостей выполнить с помощью Java/Annotation based конфигурации.
 * Для обработки запросов использовать @Controller и/или @RestController.
 * В качестве движка шаблонов использовать Thymeleaf.
 * Запустить веб приложение во внешнем веб сервере.
 * Авторизацию и аутентификацию делать не надо.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    //https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }



    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[]{encodingFilter};
    }

    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        final DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
}