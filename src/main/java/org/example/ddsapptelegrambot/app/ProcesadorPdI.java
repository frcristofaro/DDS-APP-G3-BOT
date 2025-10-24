package org.example.ddsapptelegrambot.app;

import org.example.ddsapptelegrambot.app.dtos.PdIDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public PdIDTO crearPdi(PdIDTO nuevoPdi) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PdIDTO> request = new HttpEntity<>(nuevoPdi, headers);

            ResponseEntity<PdIDTO> response =
                    restTemplate.postForEntity(BASE_URL, request, PdIDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error al crear el PdI: " + e.getStatusCode());
            return null;
        } catch (Exception e) {
            System.out.println("Error inesperado al crear PdI: " + e.getMessage());
            return null;
        }
    }

}
