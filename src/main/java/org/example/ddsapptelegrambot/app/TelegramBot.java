package org.example.ddsapptelegrambot.app;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.ddsapptelegrambot.service.ProcesadorPdIService;
import org.example.ddsapptelegrambot.service.ProcesadorSolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private ProcesadorPdIService procesadorPdIService;

    @Autowired
    private ProcesadorSolicitudService procesadorSolicitudService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private Long adminChatId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @PostConstruct
    public void onStartup() {
        System.out.println("🤖 Bot iniciado, esperando mensajes para enviar notificaciones.");
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("📩 Mensaje recibido: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            final String text = update.getMessage().getText();
            final Long chatId = update.getMessage().getChatId();
            String respuesta;

            if (adminChatId == null) {
                adminChatId = chatId;
                sendMarkdown(adminChatId, "✅ Hola! Bot registrado correctamente y listo para usar.");
            }

            try {
                if (text.startsWith("/start")) {
                    respuesta = "👋 ¡Hola! Soy el bot del Grupo 3. Probá el comando /pdi <id> para consultar un PdI.";
                    sendMarkdown(chatId, respuesta);
                } else if (text.startsWith("/pdi")) {
                    enviarPDI(text, chatId);
                } else if (text.startsWith("/CrearSolicitud")) {
                    crearSolicitudDesdeBot(text, chatId);
                } else {
                    respuesta = "🤔 No entendí. Probá con /start, /pdi <id> o /CrearSolicitud <estado> <hechoId> <descripcion>";
                    sendMarkdown(chatId, respuesta);
                }
            } catch (Exception e) {
                sendMarkdown(chatId, "❌ Tu solicitud no se pudo procesar correctamente.");
                e.printStackTrace();
            }
        }
    }

    private void sendMarkdown(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.enableMarkdown(false);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(Long chatId, String imageUrl) {
        try {
            org.telegram.telegrambots.meta.api.methods.send.SendPhoto photo =
                    new org.telegram.telegrambots.meta.api.methods.send.SendPhoto();
            photo.setChatId(chatId.toString());
            photo.setPhoto(new org.telegram.telegrambots.meta.api.objects.InputFile(imageUrl));
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void enviarPDI(String textoRecibido, Long chatId) {
        String respuesta;
        String[] parts = textoRecibido.split(" ");
        if (parts.length == 2) {
            Long id = Long.parseLong(parts[1]);
            respuesta = procesadorPdIService.obtenerPdi(id);
            sendMarkdown(chatId, respuesta);
        } else {
            respuesta = "⚙️ Uso correcto: /pdi <id>";
            sendMarkdown(chatId, respuesta);
        }
    }

    private void crearSolicitudDesdeBot(String textoRecibido, Long chatId) {
        String[] parts = textoRecibido.split(" ", 4); // /CrearSolicitud <estado> <hechoId> <descripcion>
        if (parts.length < 4) {
            sendMarkdown(chatId, "⚙️ Uso correcto: /CrearSolicitud <estado> <hechoId> <descripcion>");
            return;
        }

        String estadoStr = parts[1];
        String hechoId = parts[2];
        String descripcion = parts[3];

        String respuesta = procesadorSolicitudService.procesarSolicitud(descripcion, hechoId, estadoStr);
        sendMarkdown(chatId, respuesta);
    }

    @PreDestroy
    public void onShutdown() {
        System.out.println("🛑 Apagando bot...");
        try {
            if (adminChatId != null) {
                SendMessage message = new SendMessage();
                message.setChatId(adminChatId.toString());
                message.setText("⚠️ El bot se apagó correctamente.");
                execute(message);
            }
        } catch (TelegramApiException e) {
            System.err.println("No se pudo notificar el apagado del bot: " + e.getMessage());
        }
    }
}