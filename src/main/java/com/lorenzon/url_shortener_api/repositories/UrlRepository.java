package com.lorenzon.url_shortener_api.repositories;

import com.lorenzon.url_shortener_api.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Url findByShortCode(String shortCode);

    void deleteByShortCode(String shortCode);
}
