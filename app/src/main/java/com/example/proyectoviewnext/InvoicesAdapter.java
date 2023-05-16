package com.example.proyectoviewnext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.InvoicesViewHolder> {
    private ArrayList<Invoice> invoices_list;

    public InvoicesAdapter(ArrayList<Invoice> invoices_list) {
        this.invoices_list = invoices_list;
    }

    public void setInvoices_list(ArrayList<Invoice> invoices_list) {
        this.invoices_list = invoices_list;
    }

    // Creación del viewholder donde estará cada factura
    @NonNull
    @Override
    public InvoicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item, parent, false);
        return new InvoicesViewHolder(v);
    }

    // Carga del layout en la vista con el listado de facturas
    @Override
    public void onBindViewHolder(@NonNull InvoicesViewHolder holder, int position) {
        Invoice invoice = invoices_list.get(position);
        holder.getText_view_date().setText(invoice.getDate());
        holder.getText_view_status().setText(invoice.getStatus());
        holder.getText_view_amount().setText(String.format("%.2f €", invoice.getAmount()));
    }

    // Devuelve el contador de elementos de la vista
    @Override
    public int getItemCount() {
        return invoices_list.size();
    }

    public class InvoicesViewHolder extends RecyclerView.ViewHolder {
        private TextView text_view_date;
        private TextView text_view_status;
        private TextView text_view_amount;

        public InvoicesViewHolder(@NonNull View v) {
            super(v);
            this.text_view_date = (TextView) v.findViewById(R.id.invoice_date);
            this.text_view_status = (TextView) v.findViewById(R.id.invoice_status);
            this.text_view_amount = (TextView) v.findViewById(R.id.invoice_amount);
        }

        public TextView getText_view_date() {
            return text_view_date;
        }

        public TextView getText_view_status() {
            return text_view_status;
        }

        public TextView getText_view_amount() {
            return text_view_amount;
        }
    }
}
