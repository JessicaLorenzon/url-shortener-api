package com.lorenzon.url_shortener_api.tests;

import com.lorenzon.url_shortener_api.entities.Url;

public class Factory {

    public static Url createUrl() {
        return new Url(1L, "https://www.google.com", "google");
    }
}
