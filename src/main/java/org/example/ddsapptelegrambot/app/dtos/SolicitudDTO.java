package org.example.ddsapptelegrambot.app.dtos;

public record SolicitudDTO(
String id,
String descripcion, 
EstadoSolicitudBorradoEnum estado, 
String hechoId) {}