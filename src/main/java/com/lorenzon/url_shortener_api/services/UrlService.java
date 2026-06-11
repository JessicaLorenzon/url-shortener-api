package com.lorenzon.url_shortener_api.services;

import com.lorenzon.url_shortener_api.dtos.UrlRequestDTO;
import com.lorenzon.url_shortener_api.dtos.UrlResponseDTO;
import com.lorenzon.url_shortener_api.dtos.UrlStatisticsDTO;
import com.lorenzon.url_shortener_api.entities.Url;
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
        url.setShortCode(generateShortCode());
        urlRepository.save(url);
        return new UrlResponseDTO(url);
    }

    @Transactional(readOnly = true)
    public UrlResponseDTO findByShortCode(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode);
        return new UrlResponseDTO(url);
    }

    @Transactional
    public UrlResponseDTO update(String shortCode, UrlRequestDTO requestDTO) {
        Url url = urlRepository.findByShortCode(shortCode);
        url.setOriginalUrl(requestDTO.originalUrl());
        url.setShortCode(generateShortCode());
        url = urlRepository.save(url);
        return new UrlResponseDTO(url);
    }

    @Transactional
    public void deleteByShortCode(String shortCode) {
        urlRepository.deleteByShortCode(shortCode);
    }

    @Transactional(readOnly = true)
    public UrlStatisticsDTO showStatistics(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode);
        return new UrlStatisticsDTO(url);
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
