package com.example.proyectoviewnext.apiservice;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.example.proyectoviewnext.MainActivity;
import com.example.proyectoviewnext.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.infinum.retromock.Retromock;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InvoiceApiService {

    private static InvoiceApiServiceRequest apiService;

    private static final String BASE_URL = AppConstants.BASE_URL;

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
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, localDateJsonDeserializer).create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient.build()).build();
            apiService = retrofit.create(InvoiceApiServiceRequest.class);
            createRetromockData(retrofit);
        }
        return apiService;
    }

    /**
     * Carga los datos ficticios gestionados con retromock en el retrofit pasado por parámetro
     *
     * @param retrofit La carga de datos recibida
     */
    public static void createRetromockData(Retrofit retrofit) {
        apiService = new Retromock.Builder().retrofit(retrofit).defaultBodyFactory(new ResourceBodyFactory()).build().create(InvoiceApiServiceRequest.class);
        Log.d("testing", "Entro en createRetromockData");
    }

    public static final JsonDeserializer<LocalDate> localDateJsonDeserializer = (jsonElem, type, context) -> {
        if (jsonElem == null) {
            return null;
        }
        String localDateStr = jsonElem.getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.API_DATE_FORMAT);
        return LocalDate.parse(localDateStr, formatter);
    };
}

