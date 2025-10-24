package org.example.ddsapptelegrambot.service.procesadorPdi;

import org.example.ddsapptelegrambot.dtos.PdIDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProcesadorPdI {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://dds-app-procesador.onrender.com/pdis/";

    public PdIDTO obtenerPdiPorId(Long id) {
        try {
            return restTemplate.getForObject(BASE_URL + id, PdIDTO.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Error al consultar el PdI: " + e.getStatusCode());
            return null;
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }

}
