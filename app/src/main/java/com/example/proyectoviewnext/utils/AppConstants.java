package com.example.proyectoviewnext.utils;

public final class AppConstants {
    // General
    public static final String DATE_FORMAT = "dd MMM yyyy";
    public static final String API_DATE_FORMAT = "dd/MM/yyyy";
    public static final String API_DATE_LANGUAGE = "es";
    public static final String API_DATE_COUNTRY = "ES";

    // Filter
    public static final String DATE_BUTTON = "día/mes/año";
    public static final int AMOUNT_PORTION = 5;
    // Retrofit
    public static final String BASE_URL = "https://viewnextandroid2.wiremockapi.cloud/";
    public static final String URL_PATH = "facturas";

    /**
     * Constructor privado para ocultar el constructor público implícito
     */
    private AppConstants() {
    }

}
