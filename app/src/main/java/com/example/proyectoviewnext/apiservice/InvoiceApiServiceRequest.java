package com.example.proyectoviewnext.apiservice;

import com.example.proyectoviewnext.invoice.InvoicesList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoiceApiServiceRequest {
    @GET("facturas")
    Call<InvoicesList> getInvoices();
}
