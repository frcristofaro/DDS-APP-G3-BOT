package org.example.ddsapptelegrambot;

import org.example.ddsapptelegrambot.app.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class DdsAppTelegramBotApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DdsAppTelegramBotApplication.class, args);
        TelegramBot bot = context.getBean(TelegramBot.class);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Bot iniciado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
