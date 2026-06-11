package com.lorenzon.url_shortener_api.dtos;

import com.lorenzon.url_shortener_api.entities.Url;

import java.time.Instant;

public record UrlResponseDTO(Long id, String originalUrl, String shortCode, Instant createdAt, Instant updatedAt) {

    public UrlResponseDTO(Url entity) {
        this(entity.getId(), entity.getOriginalUrl(), entity.getShortCode(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
