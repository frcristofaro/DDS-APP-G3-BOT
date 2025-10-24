package org.example.ddsapptelegrambot.app;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.ddsapptelegrambot.service.ProcesadorPdIService;
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

    //Mensaje Inicial al correr el programa
    @PostConstruct
    public void onStartup() {
        System.out.println("🤖 Bot iniciado, esperando mensajes para enviar notificaciones.");
        // No enviamos mensaje hasta recibir un chatId válido
    }

    @Override
    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {

        System.out.println("📩 Mensaje recibido: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            final String text = update.getMessage().getText();
            final Long chatId = update.getMessage().getChatId();
            String respuesta;

            // Guardamos el primer chatId válido como admin
            if (adminChatId == null) {
                adminChatId = chatId;
                sendMarkdown(adminChatId, "✅ Hola! Bot registrado correctamente y listo para usar.");
            }

            try {
                if (text.startsWith("/start")) {
                    respuesta = "👋 ¡Hola! Soy el bot del Grupo 3. Probá el comando /pdi <id> para consultar un PdI.";
                } else if (text.startsWith("/pdi")) {
                    enviarPDI(text, chatId);
                } else {
                    // 👇 respuesta genérica si el comando no se reconoce
                    respuesta = "🤔 No entendí. Probá con /start o /pdi <id>.";
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

    //Mensaje al finalizar el programa
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

    public void enviarPDI (String textoRecibido,Long chatId) {
        String respuesta;
        String[] parts = textoRecibido.split(" ");
        if (parts.length == 2) {
            Long id = Long.parseLong(parts[1]);
            respuesta = procesadorPdIService.obtenerPdi(id);
            sendMarkdown(chatId, respuesta);

            // Intentar enviar imagen si existe
//            String url = procesadorPdIService.obtenerImagenPdI(id);
//            if (url != null) sendPhoto(chatId, url);
//            return;
        } else {
            respuesta = "⚙️ Uso correcto: /pdi <id>";
        }
    }
}
