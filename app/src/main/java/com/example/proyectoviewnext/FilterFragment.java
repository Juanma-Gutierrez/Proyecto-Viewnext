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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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


    public FilterFragment(Filter filter) {
        this.filter = filter;
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
        Log.d("debug","onCreateView()");
        // Rellenar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        // Preparación del toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_filter);
        toolbar.setTitle(R.string.filter_fragment_title);
        // Asignación de listeners
        setClickListeners(view);
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
        Log.d("debug","setClickListeners()");
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
    }

    /**
     * Aplicar el filtro, graba en el objeto filter la configuración de FilterFragment
     */
    private void applyFilter() {
        // TODO Implementar la aplicación del filtro
        Log.d("debug", "applyFilter()");
        filter.setDate_from(filter.getDate_from_temp());
        filter.setDate_until(filter.getDate_until_temp());
        filter.setPaid(paid.isChecked());
        filter.setCancelled(cancelled.isChecked());
        filter.setFixed_fee(fixed_fee.isChecked());
        filter.setPending_payment(pending_payment.isChecked());
        filter.setPayment_plan(payment_plan.isChecked());
        closeFragment();
    }

    /**
     * Borrado de todos los filtros
     */
    private void deleteFilter() {
        Log.d("debug", "deleteFilter()");
        filter.resetFilter();
        loadValues(filter);
    }

    /**
     * Carga los valores del objeto filter en FilterFragment
     *
     * @param filter Objeto filter con la configuración del filtro a aplicar
     */
    private void loadValues(Filter filter) {
        Log.d("debug", "loadValues()");
        date_from_button.setText((filter.getDate_from() == null) ?
                AppConstants.DATE_BUTTON :
                dateFormat(filter.getDate_from()));
        date_until_button.setText((filter.getDate_until() == null) ?
                AppConstants.DATE_BUTTON :
                dateFormat(filter.getDate_until()));
        paid.setChecked(filter.isPaid());
        cancelled.setChecked(filter.isCancelled());
        fixed_fee.setChecked(filter.isFixed_fee());
        pending_payment.setChecked(filter.isPending_payment());
        payment_plan.setChecked(filter.isPayment_plan());
    }

    public String dateFormat(Date date) {
        Log.d("debug","dateFormat()");
        SimpleDateFormat new_format = new SimpleDateFormat(AppConstants.API_DATE_FORMAT, new Locale(AppConstants.API_DATE_LANGUAGE, AppConstants.API_DATE_COUNTRY));
        String new_date = new_format.format(date);
        return new_date;
    }

    /**
     * Cierra FilterFragment
     */
    private void closeFragment() {
        Log.d("debug","closeFragment()");
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(this)
                .commit();
    }

    /**
     * Log con información del objeto filtro
     *
     * @param msg Mensaje a mostrar por consola
     */
    private void filterLog(String msg) {
        Log.d("debug", msg + " -> datefrom:" + filter.getDate_from() +
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
}