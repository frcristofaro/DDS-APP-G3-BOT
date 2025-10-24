package org.example.ddsapptelegrambot.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PdIDTO {
    private String id;
    private String hecho_id;
    private String descripcion;
    private String lugar;
    private String momento;
    private String contenido;
    private String url_imagen;
    private String ocr_resultado;
    private List<String> etiquetas;
}
