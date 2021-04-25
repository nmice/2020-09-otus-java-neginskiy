package ru.neginskiy.telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * За несколько дней до защиты скинуть ПР
 * Проект в отдельной ветке
 * 1 - Тема:
 * Telegram-бот для быстрого отслеживания количества аудитории социальных сетей.
 * 2 - Основная функция:
 * Экономия времени на сбор данных из указанных соцсетей по аккаунту и выдача краткой информации по кол-ву подписчиков через Telegram.
 * 3 - Примерный стек технологий:
 * java 11
 * Hibernate
 * Spring-Boot
 * Gradle
 * Rest API
 * Telegram-Bot-API
 * 4 - План по датам:
 * - 19.04.2021-25.04.2021 - изучение api, написание кода, деплой готового бота
 * - 26.04.2021-30.04.2021 - отладка, тестирование
 * Cбор данных по аккаунту планирую делать с помощью открытых api соцсетей или библиотек по работе с api.
 * Хотел бы сделать интеграцию с Instagram(API Instagram Basic Display/Instagram4j).
 */
public class SubsCounterBot extends TelegramLongPollingBot{

    public static void main(String[] args) {
    }

    @Override
    public String getBotUsername() {
        return "SubsCounterBot";
    }

    @Override
    public String getBotToken() {
        return "1743065351:AAHIiCdVfAMJftMmw_vXWb_Rcg_ExB6_PM8";
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
