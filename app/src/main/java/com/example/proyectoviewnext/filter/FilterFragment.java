package com.example.proyectoviewnext.filter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.proyectoviewnext.utils.AppConstants;
import com.example.proyectoviewnext.invoice.Invoice;
import com.example.proyectoviewnext.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FilterFragment extends Fragment {
    private Button dateFromButton;
    private Button dateUntilButton;
    private SeekBar amountSeekbar;
    private int amountSeekbarSelected;
    private TextView amountMaxTitle;
    private TextView amountMaxSelected;
    private CheckBox paid;
    private CheckBox cancelled;
    private CheckBox fixedFee;
    private CheckBox pendingPayment;
    private CheckBox paymentPlan;
    private Button deleteFilter;
    private Button applyFilter;
    private Filter filter;
    private OnButtonClickListener buttonClickListener;
    private ArrayList<Invoice> invoicesList; // Lista de facturas
    private ArrayList<Invoice> filteredList; // Lista filtrada de facturas

    public FilterFragment(Filter filter, ArrayList<Invoice> invoicesList) {
        this.filter = filter;
        this.invoicesList = invoicesList;
    }

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Rellenar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        // Preparación del toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_filter);
        toolbar.setTitle(R.string.filter_fragment_title);
        // Asignación de listeners
        setClickListeners(view);
        // Control de amount_seekbar
        amountControl();
        // Carga los valores de filter
        loadValues(filter);
        return view;
    }

    /**
     * Activa los listeners para todos los elementos de FilterFragment
     *
     * @param view
     */
    public void setClickListeners(View view) {
        // Obtener referencias a los elementos del diseño
        dateFromButton = view.findViewById(R.id.date_from_button);
        dateUntilButton = view.findViewById(R.id.date_until_button);
        amountSeekbar = view.findViewById(R.id.filter_amount_seekbar);
        amountMaxTitle = view.findViewById(R.id.filter_amount_max);
        amountMaxSelected = view.findViewById(R.id.filter_amount_max_selected);
        paid = view.findViewById(R.id.checkbox_paid);
        cancelled = view.findViewById(R.id.checkbox_cancelled);
        fixedFee = view.findViewById(R.id.checkbox_fixed_fee);
        pendingPayment = view.findViewById(R.id.checkbox_pending_payment);
        paymentPlan = view.findViewById(R.id.checkbox_payment_plan);
        deleteFilter = view.findViewById(R.id.filter_delete_filters_button);
        applyFilter = view.findViewById(R.id.filter_apply_button);
        deleteFilter.setOnClickListener(v -> deleteFilter());
        applyFilter.setOnClickListener(v -> applyFilter());
    }

    /**
     * Aplicar el filtro, graba en el objeto filter la configuración de FilterFragment
     */
    private void applyFilter() {
        filter.setAmountSelected(amountSeekbarSelected);
        filter.setDateFrom(filter.getDateFromTemp());
        filter.setDateUntil(filter.getDateUntilTemp());
        filter.setPaid(paid.isChecked());
        filter.setCancelled(cancelled.isChecked());
        filter.setFixedFee(fixedFee.isChecked());
        filter.setPendingPayment(pendingPayment.isChecked());
        filter.setPaymentPlan(paymentPlan.isChecked());
        // hacer el filtrado total
        filteredList = new ArrayList<Invoice>();
        applyFilterToInvoicesList(filteredList);
        if (buttonClickListener != null) {
            buttonClickListener.onButtonClicked(filteredList);
        }
        closeFragment();
    }

    /**
     * Aplica el filtro a filteredList y añade cada elemento que cumple el filtro pasado
     *
     * @param filteredList Lista filtrada donde añadir los elementos que cumplan el filtro
     */
    private void applyFilterToInvoicesList(ArrayList<Invoice> filteredList) {
        for (Invoice i : invoicesList) {
            boolean match = true;
            // Chequea fecha de factura
            if ((filter.getDateFrom() != null && i.getDateAsDate().before(filter.getDateFrom())) || (filter.getDateUntil() != null && i.getDateAsDate().after(filter.getDateUntil()))) {
                match = false;
            }
            // Chequea importe de factura
            match = match && i.getAmount() <= filter.getAmountSelected();
            // Chequea estado de la factura
            ArrayList<String> status = new ArrayList<>();
            if (filter.isPaid()) {
                status.add(getActivity().getApplicationContext().getString(R.string.filter_fragment_paid));
            }
            if (filter.isCancelled()) {
                status.add(getActivity().getApplicationContext().getString(R.string.filter_fragment_cancelled));
            }
            if (filter.isFixedFee()) {
                status.add(getActivity().getApplicationContext().getString(R.string.filter_fragment_fixed_fee));
            }
            if (filter.isPendingPayment()) {
                status.add(getActivity().getApplicationContext().getString(R.string.filter_fragment_pending_payment));
            }
            if (filter.isPaymentPlan()) {
                status.add(getActivity().getApplicationContext().getString(R.string.filter_fragment_status_payment_plan));
            }
            match = (match && status.contains(i.getStatus()) || (match && status.isEmpty()));

            // Comprobar el match para añadir a filteredList
            if (match) {
                filteredList.add(i);
            }
        }
        if (filteredList.isEmpty()) {
            getActivity().findViewById(R.id.filter_none_invoices_info).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.filter_none_invoices_info).setVisibility(View.GONE);
        }
    }

    /**
     * Borrado de todos los filtros
     */
    private void deleteFilter() {
        filter.resetFilter();
        loadValues(filter);
    }

    /**
     * Carga los valores del objeto filter en FilterFragment
     *
     * @param filter Objeto filter con la configuración del filtro a aplicar
     */
    private void loadValues(Filter filter) {
        dateFromButton.setText((filter.getDateFrom() == null) ?
                AppConstants.DATE_BUTTON :
                dateFormat(filter.getDateFrom()));
        dateUntilButton.setText((filter.getDateUntil() == null) ?
                AppConstants.DATE_BUTTON :
                dateFormat(filter.getDateUntil()));
        amountSeekbar.setMax((int) filter.getMaxAmount());
        amountSeekbar.setProgress(filter.getAmountSelected());
        amountMaxTitle.setText(String.valueOf(filter.getMaxAmount()) + " €");
        paid.setChecked(filter.isPaid());
        cancelled.setChecked(filter.isCancelled());
        fixedFee.setChecked(filter.isFixedFee());
        pendingPayment.setChecked(filter.isPendingPayment());
        paymentPlan.setChecked(filter.isPaymentPlan());
    }

    /**
     * Aplica el formato de fecha
     *
     * @param date Fecha a la que aplicar el formato
     * @return
     */
    public String dateFormat(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat(AppConstants.API_DATE_FORMAT, new Locale(AppConstants.API_DATE_LANGUAGE, AppConstants.API_DATE_COUNTRY));
        return newFormat.format(date);
    }

    /**
     * Cierra FilterFragment
     */
    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(this)
                .commit();
    }

    /**
     * Control de la seekbar de importe de factura
     */
    public void amountControl() {
        amountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amountSeekbarSelected = progress;
                amountMaxSelected.setText(String.valueOf(progress) + " €");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Método requerido, pero no se necesita implementación específica aquí.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Método requerido, pero no se necesita implementación específica aquí.
            }
        });
    }

    public interface OnButtonClickListener {
        void onButtonClicked(ArrayList<Invoice> invoices);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            buttonClickListener = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        buttonClickListener = null;
    }
}


