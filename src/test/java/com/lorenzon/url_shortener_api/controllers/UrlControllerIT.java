package com.lorenzon.url_shortener_api.controllers;

import io.restassured.config.RedirectConfig;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class UrlControllerIT {

    private String existingShortCode;
    private String nonExistingShortCode;
    private Map<String, Object> urlInstance;

    @BeforeEach
    public void setUp() {
        baseURI = "http://localhost:8080";

        existingShortCode = "github";
        nonExistingShortCode = existingShortCode + "xpto";

        urlInstance = new HashMap<>();
        urlInstance.put("originalUrl", "https://www.google.com");
    }

    @Test
    public void redirectShouldReturnPermanentRedirectWhenShortCodeExists() {
        given()
                .config(config().redirect(RedirectConfig.redirectConfig().followRedirects(false)))
                .get("/{shortCode}", existingShortCode)
                .then()
                .statusCode(308)
                .header("Location", "https://github.com");
        ;
    }

    @Test
    public void redirectShouldReturnNotFoundWhenShortCodeDoesNotExist() {
        given()
                .get("/{shortCode}", nonExistingShortCode)
                .then()
                .statusCode(404)
                .body("detail", equalTo("Short code " + nonExistingShortCode + " not found"));
    }

    @Test
    public void findAllShouldReturnOkAndListUrls() {
        given()
                .get("/shorten")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void createShouldReturnUrlCreated() {
        JSONObject newUrl = new JSONObject(urlInstance);

        given()
                .body(newUrl.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/shorten")
                .then()
                .statusCode(201)
                .body("originalUrl", equalTo("https://www.google.com"));
    }

    @Test
    public void createShortUrlShouldReturnBadRequestWhenInvalidData() {
        urlInstance.put("originalUrl", "");
        JSONObject newUrl = new JSONObject(urlInstance);

        given()
                .body(newUrl.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/shorten")
                .then()
                .statusCode(400)
                .body("detail", equalTo("Original URL cannot be blank"));
    }

    @Test
    public void retrieveOriginalUrlShouldReturnOkAndUrlWhenShortCodeExists() {
        given()
                .get("/shorten/{shortCode}", existingShortCode)
                .then()
                .statusCode(200)
                .body("shortCode", equalTo(existingShortCode))
                .body("originalUrl", equalTo("https://github.com"));

    }

    @Test
    public void retrieveOriginalUrlShouldReturnNotFoundWhenShortCodeDoesNotExist() {
        given()
                .get("/shorten/{shortCode}", nonExistingShortCode)
                .then()
                .statusCode(404)
                .body("detail", equalTo("Short code " + nonExistingShortCode + " not found"));
    }

    @Test
    public void updateShortUrlShouldReturnOkAndUrlUpdatedWhenValidDataAndShortCodeExists() {
        urlInstance.put("originalUrl", "https://updated.com");
        JSONObject newUrl = new JSONObject(urlInstance);

        given()
                .body(newUrl.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/shorten/{shortCode}", existingShortCode)
                .then()
                .statusCode(200)
                .body("id", equalTo(existingShortCode))
                .body("originalUrl", equalTo("https://updated.com"));

    }

    @Test
    public void updateShortUrlShouldReturnBadRequestWhenInvalidData() {
        urlInstance.put("originalUrl", "");
        JSONObject newUrl = new JSONObject(urlInstance);

        given()
                .body(newUrl.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/shorten/{shortCode}", existingShortCode)
                .then()
                .statusCode(400)
                .body("detail", equalTo("Original URL cannot be blank"));
    }

    @Test
    public void updateShortUrlShouldReturnNotFoundWhenShortCodeDoesNotExist() {
        JSONObject newUrl = new JSONObject(urlInstance);

        given()
                .body(newUrl.toString())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/shorten/{shortCode}", nonExistingShortCode)
                .then()
                .statusCode(404)
                .body("detail", equalTo("Short code " + nonExistingShortCode + " not found"));
    }

    @Test
    public void deleteShortUrlShouldReturnNoContentWhenShortCodeExists() {

        given()
                .delete("/shorten/{shortCode}", existingShortCode)
                .then()
                .statusCode(204);
    }

    @Test
    public void deleteShortUrlShouldReturnNotFoundWhenShortCodeDoesNotExist() {
        given()
                .delete("/shorten/{shortCode}", nonExistingShortCode)
                .then()
                .statusCode(404)
                .body("detail", equalTo("Short code " + nonExistingShortCode + " not found"));
    }

    @Test
    public void getUrlStatisticsShouldReturnOkAndUrlWhenShortCodeExists() {
        given()
                .get("/shorten/{shortCode}/stats", existingShortCode)
                .then()
                .statusCode(200)
                .body("shortCode", equalTo(existingShortCode))
                .body("accessCount", notNullValue());
    }

    @Test
    public void getUrlStatisticsShouldReturnNotFoundWhenShortCodeDoesNotExist() {
        given()
                .get("/shorten/{shortCode}/stats", nonExistingShortCode)
                .then()
                .statusCode(404)
                .body("detail", equalTo("Short code " + nonExistingShortCode + " not found"));
    }
}
