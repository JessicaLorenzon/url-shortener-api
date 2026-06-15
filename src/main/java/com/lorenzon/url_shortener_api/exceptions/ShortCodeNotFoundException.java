package com.lorenzon.url_shortener_api.exceptions;

public class ShortCodeNotFoundException extends RuntimeException {

    public ShortCodeNotFoundException(String shortCode) {

        super("Short code " + shortCode + " not found");
    }
}
