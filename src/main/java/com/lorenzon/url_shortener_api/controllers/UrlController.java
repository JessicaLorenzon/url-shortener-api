package com.lorenzon.url_shortener_api.controllers;

import com.lorenzon.url_shortener_api.dtos.UrlRequestDTO;
import com.lorenzon.url_shortener_api.dtos.UrlResponseDTO;
import com.lorenzon.url_shortener_api.dtos.UrlStatisticsDTO;
import com.lorenzon.url_shortener_api.services.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrlAndTrackAccess(shortCode);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).header("Location", originalUrl).build();
    }

    @GetMapping("/shorten")
    public ResponseEntity<List<UrlResponseDTO>> findAllUrls() {
        List<UrlResponseDTO> urls = urlService.findAll();
        return ResponseEntity.ok(urls);
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponseDTO> createShortUrl(@RequestBody @Valid UrlRequestDTO requestDTO) {
        UrlResponseDTO responseDTO = urlService.insert(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/shorten/{shortCode}")
    public ResponseEntity<UrlResponseDTO> retrieveOriginalUrl(@PathVariable String shortCode) {
        UrlResponseDTO responseDTO = urlService.findByShortCode(shortCode);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/shorten/{shortCode}")
    public ResponseEntity<UrlResponseDTO> updateShortUrl(@PathVariable String shortCode, @RequestBody @Valid UrlRequestDTO requestDTO) {
        UrlResponseDTO responseDTO = urlService.update(shortCode, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        urlService.deleteByShortCode(shortCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shorten/{shortCode}/stats")
    public ResponseEntity<UrlStatisticsDTO> getUrlStatistics(@PathVariable String shortCode) {
        UrlStatisticsDTO responseDTO = urlService.showStatistics(shortCode);
        return ResponseEntity.ok(responseDTO);
    }
}
