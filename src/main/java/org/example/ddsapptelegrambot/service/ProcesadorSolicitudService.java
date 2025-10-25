package org.example.ddsapptelegrambot.service;

import org.example.ddsapptelegrambot.app.ProcesadorSolicitud;
import org.example.ddsapptelegrambot.app.dtos.SolicitudDTO;
import org.example.ddsapptelegrambot.app.dtos.EstadoSolicitudBorradoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcesadorSolicitudService {

    @Autowired
    private ProcesadorSolicitud solicitudClient;

    public String procesarSolicitud(String descripcion, String hechoId, String estadoStr) {
    EstadoSolicitudBorradoEnum estado;

    try {
        estado = EstadoSolicitudBorradoEnum.valueOf(estadoStr.toUpperCase());
    } catch (IllegalArgumentException e) {
        return "⚠️ Estado inválido. Usá uno de: CREADA, VALIDADA, EN_DISCUCION, ACEPTADA, RECHAZADA.";
    }

    SolicitudDTO dto = new SolicitudDTO(null, descripcion, estado, hechoId);
    SolicitudDTO creada = solicitudClient.crearSolicitud(dto);

    if (creada == null) {
        return "❌ No se pudo crear la solicitud.";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("✅ *Solicitud creada con éxito!*\n")
      .append("*ID:* ").append(creada.id()).append("\n")
      .append("*Descripción:* ").append(creada.descripcion()).append("\n")
      .append("*Estado:* ").append(creada.estado()).append("\n")
      .append("*Hecho ID:* ").append(creada.hechoId());

    return sb.toString();
    }

}