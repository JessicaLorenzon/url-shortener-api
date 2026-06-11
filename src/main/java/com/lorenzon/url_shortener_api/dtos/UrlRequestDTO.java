package com.lorenzon.url_shortener_api.dtos;

import jakarta.validation.constraints.NotBlank;

public record UrlRequestDTO(
        @NotBlank
        String originalUrl) {
}
