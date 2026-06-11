package com.lorenzon.url_shortener_api.dtos;

import com.lorenzon.url_shortener_api.entities.Url;

import java.time.Instant;

public record UrlStatisticsDTO(Long id, String originalUrl, String shortCode, Instant createdAt, Instant updatedAt, Long accessCount) {

    public UrlStatisticsDTO(Url entity) {
        this(entity.getId(), entity.getOriginalUrl(), entity.getShortCode(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getAccessCount());
    }
}
