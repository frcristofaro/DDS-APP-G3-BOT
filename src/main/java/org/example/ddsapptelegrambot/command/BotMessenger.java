package org.example.ddsapptelegrambot.command;

public interface BotMessenger {
    void sendMessage(Long chatId, String text);
}
