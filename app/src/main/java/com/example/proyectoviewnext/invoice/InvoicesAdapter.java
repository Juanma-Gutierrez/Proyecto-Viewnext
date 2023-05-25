package com.example.proyectoviewnext.invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoviewnext.R;
import com.example.proyectoviewnext.utils.AppConstants;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Convierte los datos de facturas a componentes visuales para cada elemento
 */
public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.InvoicesViewHolder> {
    private List<InvoiceVO> invoicesList;

    private RecyclerOnClickListener listener;

    /**
     * Inicializa el adaptador InvoicesAdapter
     *
     * @param invoicesList Lista de facturas
     */
    public InvoicesAdapter(List<InvoiceVO> invoicesList) {
        this.invoicesList = invoicesList;
    }

    /**
     * Configura la lista de facturas invoices
     *
     * @param invoicesList Lista de facturas
     */
    public void setInvoicesList(List<InvoiceVO> invoicesList) {
        this.invoicesList = invoicesList;
        notifyDataSetChanged();
    }

    /**
     * Creación del viewholder donde estará cada factura
     *
     * @param parent   El ViewGroup al que se añadirá la nueva View después de que se vincule a una
     *                 posición de adaptador.
     * @param viewType El tipo de view de la nueva View.
     * @return
     */
    @NonNull
    @Override
    public InvoicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item, parent, false);
        return new InvoicesViewHolder(v);
    }

    /**
     * Carga del layout en la vista con el listado de facturas
     *
     * @param holder   El ViewHolder que debe actualizarse para representar el contenido del elemento
     *                 en la posición dada en el conjunto de datos
     * @param position La posición del elemento dentro del conjunto de datos del adaptador
     */
    @Override
    public void onBindViewHolder(@NonNull InvoicesViewHolder holder, int position) {
        InvoiceVO invoiceVO = invoicesList.get(position);
        holder.getTextViewDate().setText(invoiceVO.getFecha().format(DateTimeFormatter.ofPattern((AppConstants.API_DATE_FORMAT))));
        holder.getTextViewStatus().setText(invoiceVO.getDescEstado());
        holder.getTextViewAmount().setText(String.format("%.2f €", invoiceVO.getImporteOrdenacion()));
    }

    public interface RecyclerOnClickListener {
        void onItemClick(int position);
    }

    public RecyclerOnClickListener setItemOnClickListener(RecyclerOnClickListener listener) {
        this.listener = listener;
        return listener;
    }

    /**
     * Devuelve el contador de elementos de la vista
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return invoicesList.size();
    }

    public static class InvoicesViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewStatus;
        private TextView textViewAmount;

        /**
         * Inicializa los atributos text_view_date, text_view_status y text_view_amount
         * con las referencias a los elementos correspondientes en la vista recibida
         *
         * @param v Vista que contiene los elementos de la factura
         */
        public InvoicesViewHolder(@NonNull View v) {
            super(v);
            this.textViewDate = (TextView) v.findViewById(R.id.invoice_date);
            this.textViewStatus = (TextView) v.findViewById(R.id.invoice_status);
            this.textViewAmount = (TextView) v.findViewById(R.id.invoice_amount);
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewStatus() {
            return textViewStatus;
        }

        public TextView getTextViewAmount() {
            return textViewAmount;
        }


    }
}
