package com.example.proyectoviewnext.apiservice;

import android.util.Log;

import com.example.proyectoviewnext.invoice.InvoicesList;
import com.example.proyectoviewnext.utils.AppConstants;

import co.infinum.retromock.meta.Mock;
import co.infinum.retromock.meta.MockResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoiceApiServiceRequest {
    @Mock
    @MockResponse(body = "response.json")
    @GET(AppConstants.URL_PATH)
    Call<InvoicesList> getInvoices();
}
