package com.example.proyectoviewnext.utils;

public final class AppConstants {
    // General
    public static final String DATE_FORMAT = "dd MMM yyyy";
    public static final String API_DATE_FORMAT = "dd/MM/yyyy";
    public static final String API_DATE_LANGUAGE = "es";
    public static final String API_DATE_COUNTRY = "ES";

    // Filter
    public static final String DATE_BUTTON = "día/mes/año";
    // AMOUNT_PORTION
    public static final int AMOUNT_PORTION = 5;

    // Retrofit
    // URL principal:   https://viewnextandroid.mocklab.io/facturas
    // URL alternativa: https://viewnextandroid2.wiremockapi.cloud/facturas
    // GitHub https://raw.githubusercontent.com/Juanma-Gutierrez/Proyecto-Viewnext/develop/json.aux
    public static final String BASE_URL = "https://raw.githubusercontent.com/Juanma-Gutierrez/Proyecto-Viewnext/develop/";
    public static final String URL_PATH = "json.aux";

    /**
     * Constructor privado para ocultar el constructor público implícito
     */
    private AppConstants() {
    }

}
