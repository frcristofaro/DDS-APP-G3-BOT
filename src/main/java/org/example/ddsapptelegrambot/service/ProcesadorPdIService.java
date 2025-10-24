package org.example.ddsapptelegrambot.service;

import org.example.ddsapptelegrambot.app.ProcesadorPdI;
import org.example.ddsapptelegrambot.app.dtos.PdIDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcesadorPdIService {

    @Autowired
    private ProcesadorPdI pdiClient;

    public String obtenerPdi(Long id) {
        PdIDTO pdi = pdiClient.obtenerPdiPorId(id);

        if (pdi == null) {
            return "No se pudo obtener el PdI con ID " + id;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("*PDI #").append(pdi.getId()).append("*\n")
                .append("*Hecho ID:* ").append(pdi.getHecho_id()).append("\n")
                .append("*Descripción:* ").append(pdi.getDescripcion()).append("\n")
                .append("*Lugar:* ").append(pdi.getLugar()).append("\n")
                .append("*Momento:* ").append(pdi.getMomento()).append("\n")
                .append("*Contenido:* ").append(pdi.getContenido()).append("\n");

        if (pdi.getUrl_imagen() != null && !pdi.getUrl_imagen().isEmpty()) {
            sb.append("\n*Imagen:* ").append(pdi.getUrl_imagen());
        }

        if (pdi.getEtiquetas() != null && !pdi.getEtiquetas().isEmpty()) {
            sb.append("\n*Etiquetas:* ").append(String.join(", ", pdi.getEtiquetas()));
        }

        return sb.toString();
    }

//    public String obtenerImagenPdI(Long id) {
//
//        PdIDTO pdi = pdiClient.obtenerPdiPorId(id);
//
//        if (pdi == null) {
//            return null;
//        }
//
//        String url = pdi.getUrl_imagen();
//        if (url == null || url.isEmpty()) {
//            return null;
//        }
//
//        return url;
//
//    }

    public String crearPdi(PdIDTO nuevoPdi) {
        PdIDTO creado = pdiClient.crearPdi(nuevoPdi);
        if (creado == null) {
            return "❌ No se pudo crear el PdI.";
        }

        return String.format("""
                ✅ PdI creado correctamente:
                🆔 ID: %s
                🏷️ %s
                📍 %s
                📅 %s
                💬 %s
                """,
                creado.getId(),
                creado.getDescripcion(),
                creado.getLugar(),
                creado.getMomento(),
                creado.getContenido()
        );
    }

}
