package com.lorenzon.url_shortener_api.services;

import com.lorenzon.url_shortener_api.dtos.UrlRequestDTO;
import com.lorenzon.url_shortener_api.dtos.UrlResponseDTO;
import com.lorenzon.url_shortener_api.dtos.UrlStatisticsDTO;
import com.lorenzon.url_shortener_api.entities.Url;
import com.lorenzon.url_shortener_api.exceptions.ShortCodeNotFoundException;
import com.lorenzon.url_shortener_api.repositories.UrlRepository;
import com.lorenzon.url_shortener_api.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @InjectMocks
    private UrlService urlService;

    @Mock
    private UrlRepository urlRepository;

    private String originalUrl;
    private String existingShortCode;
    private String nonExistingShortCode;
    private Url url;
    private UrlRequestDTO urlRequestDTO;

    @BeforeEach
    public void setUp() {
        originalUrl = "https://www.google.com";
        existingShortCode = "google";
        nonExistingShortCode = existingShortCode + "xpto";

        url = Factory.createUrl();
        urlRequestDTO = new UrlRequestDTO(originalUrl);
    }

    @Test
    public void insertShouldReturnUrlResponseDTO() {
        Mockito.when(urlRepository.save(any())).thenReturn(url);
        Mockito.when(urlRepository.existsByShortCode(any())).thenReturn(false);

        UrlResponseDTO result = urlService.insert(urlRequestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(url.getId(), result.id());
        Assertions.assertEquals(url.getOriginalUrl(), result.originalUrl());
        Assertions.assertEquals(url.getShortCode(), result.shortCode());

        Mockito.verify(urlRepository).save(any(Url.class));
    }

    @Test
    public void findByShortCodeShouldReturnUrlResponseDTOWhenShortCodeExists() {
        Mockito.when(urlRepository.findByShortCode(existingShortCode)).thenReturn(Optional.of(url));

        UrlResponseDTO result = urlService.findByShortCode(existingShortCode);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(url.getId(), result.id());
        Assertions.assertEquals(url.getShortCode(), result.shortCode());

        Mockito.verify(urlRepository).findByShortCode(existingShortCode);
    }

    @Test
    public void findByShortCodeShouldReturnShortCodeNotFoundExceptionWhenShortCodeDoesNotExist() {
        Mockito.when(urlRepository.findByShortCode(nonExistingShortCode)).thenReturn(Optional.empty());

        Assertions.assertThrows(ShortCodeNotFoundException.class, () -> {
            urlService.findByShortCode(nonExistingShortCode);
        });
    }

    @Test
    public void updateShouldReturnUrlResponseDTOWhenShortCodeExists() {
        Mockito.when(urlRepository.findByShortCode(existingShortCode)).thenReturn(Optional.of(url));

        UrlResponseDTO result = urlService.update(existingShortCode, urlRequestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(url.getId(), result.id());
        Assertions.assertEquals(originalUrl, result.originalUrl());

        Mockito.verify(urlRepository).findByShortCode(existingShortCode);
    }

    @Test
    public void updateShouldReturnShortCodeNotFoundExceptionWhenShortCodeDoesNotExist() {
        Mockito.when(urlRepository.findByShortCode(nonExistingShortCode)).thenReturn(Optional.empty());

        Assertions.assertThrows(ShortCodeNotFoundException.class, () -> {
            urlService.update(nonExistingShortCode, urlRequestDTO);
        });
    }

    @Test
    public void deleteByShortCodeShouldDoNothingWhenShortCodeExists() {
        Mockito.when(urlRepository.findByShortCode(existingShortCode)).thenReturn(Optional.of(url));

        Assertions.assertDoesNotThrow(() -> {
            urlService.deleteByShortCode(existingShortCode);
        });

        Mockito.verify(urlRepository).delete(url);
    }

    @Test
    public void deleteByShortCodeShouldReturnShortCodeNotFoundExceptionWhenShortCodeDoesNotExist() {
        Mockito.when(urlRepository.findByShortCode(nonExistingShortCode)).thenReturn(Optional.empty());

        Assertions.assertThrows(ShortCodeNotFoundException.class, () -> {
            urlService.deleteByShortCode(nonExistingShortCode);
        });
    }

    @Test
    public void showStatisticsShouldReturnUrlStatisticsDTOWhenShortCodeExists() {
        Mockito.when(urlRepository.findByShortCode(existingShortCode)).thenReturn(Optional.of(url));

        UrlStatisticsDTO result = urlService.showStatistics(existingShortCode);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(url.getId(), result.id());
        Assertions.assertEquals(url.getAccessCount(), result.accessCount());

        Mockito.verify(urlRepository).findByShortCode(existingShortCode);
    }

    @Test
    public void showStatisticsShouldReturnShortCodeNotFoundExceptionWhenShortCodeDoesNotExist() {
        Mockito.when(urlRepository.findByShortCode(nonExistingShortCode)).thenReturn(Optional.empty());

        Assertions.assertThrows(ShortCodeNotFoundException.class, () -> {
            urlService.showStatistics(nonExistingShortCode);
        });
    }

    @Test
    public void getOriginalUrlAndTrackAccessShouldReturnUrlStringAndIncrementAccessCountWhenShortCodeExists() {
        Mockito.when(urlRepository.findByShortCode(existingShortCode)).thenReturn(Optional.of(url));

        Long accessCountBefore = url.getAccessCount();

        String originalUrl = urlService.getOriginalUrlAndTrackAccess(existingShortCode);

        Assertions.assertNotNull(originalUrl);
        Assertions.assertEquals(url.getOriginalUrl(), originalUrl);
        Assertions.assertEquals(accessCountBefore + 1L, url.getAccessCount());
    }

    @Test
    public void getOriginalUrlAndTrackAccessShouldReturnShortCodeNotFoundExceptionWhenShortCodeDoesNotExist() {
        Mockito.when(urlRepository.findByShortCode(nonExistingShortCode)).thenReturn(Optional.empty());

        Assertions.assertThrows(ShortCodeNotFoundException.class, () -> {
            urlService.getOriginalUrlAndTrackAccess(nonExistingShortCode);
        });
    }

    @Test
    public void findAllShouldReturnListOfUrlResponseDTO() {
        Mockito.when(urlRepository.findAll()).thenReturn(List.of(url));

        List<UrlResponseDTO> result = urlService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(url.getId(), result.getFirst().id());
        Assertions.assertEquals(url.getOriginalUrl(), result.getFirst().originalUrl());

        Mockito.verify(urlRepository).findAll();
    }
}
