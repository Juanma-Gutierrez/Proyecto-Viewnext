package com.example.proyectoviewnext.filter;

import com.example.proyectoviewnext.invoice.InvoicesList;
import com.example.proyectoviewnext.utils.AppConstants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InvoiceApiServiceRequest {
    @GET(AppConstants.URL_PATH)
    Call<InvoicesList> getInvoices();
}
