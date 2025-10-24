package org.example.ddsapptelegrambot.app;

import jakarta.ws.rs.ext.ParamConverter;
import org.example.ddsapptelegrambot.command.*;
import org.example.ddsapptelegrambot.service.procesadorPdi.ProcesadorPdIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot implements BotMessenger {

    private final CommandDispatcher commandDispatcher;
    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public TelegramBot(@Lazy CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            commandDispatcher.handle(text, chatId);
        }
    }

    @Override
    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
