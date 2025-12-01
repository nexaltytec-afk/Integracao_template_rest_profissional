package br.com.nexalty.template_rest_profissional.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {

    private Integer erro;
    private String mensagem;
    private Object dados;

    @JsonGetter("timestamp_iso")
    public String getTimestampIso() {
        return LocalDateTime.now().toString();
    }}
