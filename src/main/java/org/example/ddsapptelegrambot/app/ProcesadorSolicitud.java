package org.example.ddsapptelegrambot.app;

import org.example.ddsapptelegrambot.app.dtos.SolicitudDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProcesadorSolicitud {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://dds-app-solicitudes.onrender.com/api/solicitudes";

    public SolicitudDTO crearSolicitud(SolicitudDTO dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SolicitudDTO> request = new HttpEntity<>(dto, headers);
            return restTemplate.postForObject(BASE_URL, request, SolicitudDTO.class);
        } catch (Exception e) {
            System.out.println("Error al crear solicitud: " + e.getMessage());
            return null;
        }
    }
}