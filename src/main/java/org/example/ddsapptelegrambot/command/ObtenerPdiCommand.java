package org.example.ddsapptelegrambot.command;

import org.example.ddsapptelegrambot.service.procesadorPdi.ProcesadorPdIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ObtenerPdiCommand implements BotCommand {

    private final BotMessenger messenger;
    private final ProcesadorPdIService pdiService;

    @Autowired
    public ObtenerPdiCommand(BotMessenger messenger, ProcesadorPdIService pdiService) {
        this.messenger = messenger;
        this.pdiService = pdiService;
    }

    @Override
    public boolean canHandle(String message) {
        return message.startsWith("/pdi");
    }

    @Override
    public void handle(String message, Long chatId) {
        String[] parts = message.split(" ");


        try {
            Long id = Long.parseLong(parts[1]);
            String info = pdiService.obtenerPdi(id);
            messenger.sendMessage(chatId, "📍 PDI encontrado: " + info);
        } catch (Exception e) {
            messenger.sendMessage(chatId, "⚠️ Error al obtener el PDI: " + e.getMessage());
        }
    }
}
