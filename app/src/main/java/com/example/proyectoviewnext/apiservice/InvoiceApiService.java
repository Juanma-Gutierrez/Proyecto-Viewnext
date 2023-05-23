package com.example.proyectoviewnext.apiservice;

import com.example.proyectoviewnext.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InvoiceApiService {

    private static InvoiceApiServiceRequest apiService;

    /**
     * URL principal:   https://viewnextandroid.mocklab.io/facturas
     * URL alternativa: https://viewnextandroid2.wiremockapi.cloud/facturas
     */
    private static final String BASE_URL = "https://viewnextandroid2.wiremockapi.cloud/";

    private InvoiceApiService() {
        // Constructor privado requerido
    }

    public static InvoiceApiServiceRequest getApiService() {
        // Creamos un interceptor y le indicamos el log level a usar
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (apiService == null) {
            Gson gson = new GsonBuilder().setDateFormat(AppConstants.API_DATE_FORMAT).create();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient.build()).build();
            apiService = retrofit.create(InvoiceApiServiceRequest.class);
        }

        return apiService;
    }

}

