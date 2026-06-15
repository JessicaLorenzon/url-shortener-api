package com.lorenzon.url_shortener_api.services;

import com.lorenzon.url_shortener_api.dtos.UrlRequestDTO;
import com.lorenzon.url_shortener_api.dtos.UrlResponseDTO;
import com.lorenzon.url_shortener_api.dtos.UrlStatisticsDTO;
import com.lorenzon.url_shortener_api.entities.Url;
import com.lorenzon.url_shortener_api.exceptions.ShortCodeNotFoundException;
import com.lorenzon.url_shortener_api.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Transactional(readOnly = true)
    public List<UrlResponseDTO> findAll() {
        List<Url> urls = urlRepository.findAll();
        return urls.stream().map(UrlResponseDTO::new).toList();
    }

    @Transactional
    public UrlResponseDTO insert(UrlRequestDTO requestDTO) {
        Url url = new Url();
        url.setOriginalUrl(requestDTO.originalUrl());
        url.setShortCode(generateUniqueShortCode());
        urlRepository.save(url);
        return new UrlResponseDTO(url);
    }

    @Transactional(readOnly = true)
    public UrlResponseDTO findByShortCode(String shortCode) {
        Url url = findUrlByShortCode(shortCode);
        return new UrlResponseDTO(url);
    }

    @Transactional
    public UrlResponseDTO update(String shortCode, UrlRequestDTO requestDTO) {
        Url url = findUrlByShortCode(shortCode);
        url.setOriginalUrl(requestDTO.originalUrl());
        return new UrlResponseDTO(url);
    }

    @Transactional
    public void deleteByShortCode(String shortCode) {
        Url url = findUrlByShortCode(shortCode);
        urlRepository.delete(url);
    }

    @Transactional(readOnly = true)
    public UrlStatisticsDTO showStatistics(String shortCode) {
        Url url = findUrlByShortCode(shortCode);
        return new UrlStatisticsDTO(url);
    }

    private Url findUrlByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortCodeNotFoundException(shortCode));
    }

    private String generateUniqueShortCode() {
        String shortCode = generateShortCode();

        while (urlRepository.existsByShortCode(shortCode)) {
            shortCode = generateShortCode();
        }

        return shortCode;
    }

    private String generateShortCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int cnt = 6;

        char[] ret = new char[cnt];
        for (int i = 0; i < cnt; i++) {
            ret[i] = chars.charAt((int) (Math.random() * chars.length()));
        }

        return new String(ret);
    }
}
