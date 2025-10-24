package org.example.ddsapptelegrambot.command;

public interface BotCommand {
    boolean canHandle(String message);
    void handle(String message, Long chatId);
}
