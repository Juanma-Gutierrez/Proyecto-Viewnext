package com.example.proyectoviewnext;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Date;

public class FilterFragment extends Fragment {
    private Button date_from_button;
    private Button date_until_button;
    private Button filter_apply_button;
    private Button filter_delete_filters_button;
    private Date date_from;
    private Date date_until;
    private int max_amount;
    private CheckBox paid;
    private CheckBox cancelled;
    private CheckBox fixed_fee;
    private CheckBox pending_payment;
    private CheckBox payment_plan;
    private Button delete_filter;
    private Button apply_filter;
    private Filter filter;
    private final String button_text = AppConstants.DATE_BUTTON;

    public FilterFragment(Filter filter) {
        this.filter = filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Rellenar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_filter);
        toolbar.setTitle(R.string.filter_fragment_title);

        // Obtener referencias a los elementos del diseño
        date_from_button = view.findViewById(R.id.date_from_button);
        date_until_button = view.findViewById(R.id.date_until_button);
        // TODO falta implementar el resto de filtros
        //max_amount = view.findViewById(R.id.filter_amount_max);
        paid = view.findViewById(R.id.checkbox_paid);
        cancelled = view.findViewById(R.id.checkbox_cancelled);
        fixed_fee = view.findViewById(R.id.checkbox_fixed_fee);
        pending_payment = view.findViewById(R.id.checkbox_pending_payment);
        payment_plan = view.findViewById(R.id.checkbox_payment_plan);
        delete_filter = view.findViewById(R.id.filter_delete_filters_button);
        apply_filter = view.findViewById(R.id.filter_apply_button);
        delete_filter.setOnClickListener(v -> deleteFilter());
        apply_filter.setOnClickListener(v -> applyFilter());

        loadValues(filter);
        return view;
    }

    private void applyFilter() {
        // TODO Implementar la aplicación del filtro
        Log.d("applyFilter", "applyFilter");
        filterLog("Aplicado el filtro entrada");
        filter.setDate_from(filter.getDate_from_temp());
        filter.setDate_until(filter.getDate_until_temp());
        filter.setPaid(paid.isChecked());
        filter.setCancelled(cancelled.isChecked());
        filter.setFixed_fee(fixed_fee.isChecked());
        filter.setPending_payment(pending_payment.isChecked());
        filter.setPayment_plan(payment_plan.isChecked());
        filterLog("Aplicado el filtro salida");
        closeFragment();
    }

    private void deleteFilter() {
        Log.d("deleteFilter", "deleteFilter");
        filter.resetFilter();
        loadValues(filter);
        closeFragment();
    }

    private void loadValues(Filter filter) {
        Log.d("loadValues", "loadValues");
        if (date_from == null) {
            date_from_button.setText(button_text);
        } else {
            date_from_button.setText((CharSequence) filter.getDate_from());
        }
        if (date_until == null) {
            date_until_button.setText(button_text);
        } else {
            date_until_button.setText((CharSequence) filter.getDate_from());
        }
        paid.setChecked(filter.isPaid());
        cancelled.setChecked(filter.isCancelled());
        fixed_fee.setChecked(filter.isFixed_fee());
        pending_payment.setChecked(filter.isPending_payment());
        payment_plan.setChecked(filter.isPayment_plan());
    }


    private void filterLog(String msg) {
        Log.d("filter", msg + " -> datefrom:" + filter.getDate_from() +
                ", dateuntil:" + filter.getDate_until() +
                ", datefromtemp:" + filter.getDate_from_temp() +
                ", dateuntiltemp:" + filter.getDate_until_temp() +
                ", maxamount:" + filter.getMax_amount() +
                ", paid:" + filter.isPaid() +
                ", cancelled:" + filter.isCancelled() +
                ", fixed_fee:" + filter.isFixed_fee() +
                ", pending_payment:" + filter.isPending_payment() +
                ", payment_plan:" + filter.isPayment_plan());
    }

    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(this)
                .commit();
    }
}