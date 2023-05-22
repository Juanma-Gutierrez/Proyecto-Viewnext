package com.example.proyectoviewnext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.proyectoviewnext.filter.Filter;
import com.example.proyectoviewnext.filter.FilterFragment;
import com.example.proyectoviewnext.invoice.Invoice;
import com.example.proyectoviewnext.invoice.InvoicesAdapter;
import com.example.proyectoviewnext.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnButtonClickListener {

    private Filter filter;                   // Filtro a aplicar
    private ArrayList<Invoice> invoiceList; // Array donde se guardarán los elementos de la lista
    private InvoicesAdapter adapter;         // Adaptador de facturas

    /**
     * Se llama cuando se crea la activity por primera vez
     *
     * @param savedInstanceState Si la activity está siendo recreada, este bundle contiene los datos
     *                           previos almacenados
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices_list);
        filter = new Filter();
        // Cargamos los datos
        init();
        setMaxAmount();
    }

    /**
     * Se llama cuando se crea el menú de opciones por primera vez
     *
     * @param menu El menú de opciones
     * @return True si el menú debe ser mostrado, falso en caso contrario
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Inicializa la carga de datos de facturas
     */
    public void init() {
        RecyclerView recyclerViewList; // Vista donde se cargan las facturas

        // Menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.invoices_list_title);
        setSupportActionBar(toolbar);

        // Asignamos a la lista la vista list_view_invoices
        recyclerViewList = (RecyclerView) findViewById(R.id.list_view_invoices);
        if (recyclerViewList != null) {
            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewList.setLayoutManager(llm);
        }

        invoiceList = new ArrayList<>();
        invoiceList.add(new Invoice("12/05/2023", "Pagada", 50.23));
        invoiceList.add(new Invoice("10/05/2023", "Cuota Fija", 30.10));
        invoiceList.add(new Invoice("08/05/2023", "Pagada", 5.75));
        invoiceList.add(new Invoice("06/05/2023", "Plan de pago", 125.50));
        invoiceList.add(new Invoice("05/05/2023", "Pendiente de pago", 50.23));
        invoiceList.add(new Invoice("01/05/2023", "Pagada", 30.10));
        invoiceList.add(new Invoice("30/04/2023", "Pagada", 5.75));
        invoiceList.add(new Invoice("22/04/2023", "Plan de pago", 125.50));
        invoiceList.add(new Invoice("12/04/2023", "", 50.23));
        invoiceList.add(new Invoice("10/04/2023", "", 30.10));
        invoiceList.add(new Invoice("08/04/2023", "", 5.75));
        invoiceList.add(new Invoice("06/04/2023", "", 184.99));

        adapter = new InvoicesAdapter(invoiceList);
        recyclerViewList.setAdapter(adapter);
    }

    public void setMaxAmount() {
        double max = Integer.MIN_VALUE;
        for (Invoice i : invoiceList) {
            if (i.getAmount() > max)
                max = i.getAmount();
        }
        // Redondea el importe máximo en porciones indicadas por AMOUNT_PORTION, en este caso 50
        int roundedMax = (int) (Math.floor((max + AppConstants.AMOUNT_PORTION) / AppConstants.AMOUNT_PORTION)) * AppConstants.AMOUNT_PORTION;
        filter.setMaxAmount(roundedMax);
        filter.setAmountSelected(roundedMax);
    }

    /**
     * Diálogo al hacer click en los items
     *
     * @param v La vista donde se hizo click
     */
    //TODO VER LA POSIBILIDAD DE CAMBIAR ESTE CÓDIGO A LISTENER
    public void onItemClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_info)
                .setPositiveButton(R.string.close_button, (dialog, which) -> dialog.dismiss());
        alertDialog
                .create()
                .show();
    }

    /**
     * Abre el FilterFragment
     *
     * @param menuItem Elemento del menú
     */
    public void openFilter(MenuItem menuItem) {
        // Crear una instancia del FilterFragment
        FilterFragment filterFragment = new FilterFragment(filter, invoiceList);
        // Obtener el FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, filterFragment)
                .commit();
    }

    /**
     * Cierra el fragment filter
     *
     * @param menuItem
     */
    public MenuItem closeFilter(MenuItem menuItem) {
        // Cerrar el FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                .commit();
        return menuItem;
    }

    /**
     * Abre un calendario para capturar el día y almacenarlo en filter
     *
     * @param view Vista cargada
     */
    public void openCalendar(View view) {
        Button dateButtonFrom;
        Button dateButtonUntil;
        String buttonSelected;

        // Seleccionamos el botón pulsado y ponemos en el calendario el valor de from o until correspondiente
        int buttonId = view.getId();
        buttonSelected = (buttonId == R.id.date_from_button) ? "from" : "until";
        LocalDate currentDate = LocalDate.now();
        LocalDate buttonDate = buttonSelected.equals("from") ? filter.getDateFromTemp() : filter.getDateUntilTemp();
        if (buttonDate == null) {
            buttonDate = LocalDate.now();
        }
        // Capturamos los botones
        dateButtonFrom = findViewById(R.id.date_from_button);
        dateButtonUntil = findViewById(R.id.date_until_button);

        DatePickerDialog dpd = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
            LocalDate datePicker = LocalDate.of(year1, month1, dayOfMonth);
            if (buttonSelected.equals("from")) {
                // Comprobar si hay que rellenar until
                if (filter.getDateUntil() == null && filter.getDateUntilTemp() == null) {
                    dateButtonUntil.setText(currentDate.format(DateTimeFormatter.ofPattern(AppConstants.API_DATE_FORMAT)));
                    filter.setDateUntilTemp(currentDate);
                }
                dateButtonFrom.setText(datePicker.format(DateTimeFormatter.ofPattern(AppConstants.API_DATE_FORMAT)));
                filter.setDateFromTemp(datePicker);
            } else { // button until selected
                dateButtonUntil.setText(datePicker.format(DateTimeFormatter.ofPattern(AppConstants.API_DATE_FORMAT)));
                filter.setDateUntilTemp(datePicker);
            }
        }, buttonDate.getYear(), buttonDate.getMonthValue(), buttonDate.getDayOfMonth());
        dpd.show();
    }

    /**
     * Aplica el formato local a la fecha pasada por parámetro y la convierte en String
     *
     * @param date Fecha a aplicar el formato
     * @return String con la fecha convertida en texto en formato local
     */
    public String dateFormat(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat(AppConstants.API_DATE_FORMAT,
                new Locale(AppConstants.API_DATE_LANGUAGE,
                        AppConstants.API_DATE_COUNTRY));
        return newFormat.format(date);
    }

    /**
     * Crea un nuevo Date con los datos pasados por parámetro
     *
     * @param year       Año
     * @param month      Mes
     * @param dayOfMonth Día del mes
     * @return Date con la fecha generada
     */
    public Date getNewDate(int year, int month, int dayOfMonth) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.YEAR, year);
        newCalendar.set(Calendar.MONTH, month);
        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return newCalendar.getTime();
    }


    @Override
    public void onButtonClicked(ArrayList<Invoice> filteredInvoices) {
        adapter.setInvoicesList(filteredInvoices);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    /**
     * Log con información del filtro
     *
     * @param msg Mensaje personalizado
     */
    public void myLog(String tag, String msg) {
        Log.d("debug " + tag, msg + " -> from:" + filter.getDateFrom() +
                ", until:" + filter.getDateUntil() +
                ", fromtemp:" + filter.getDateFromTemp() +
                ", untiltemp:" + filter.getDateUntilTemp() +
                ", maxamount:" + filter.getAmountSelected() +
                ", paid:" + filter.isPaid() +
                ", cancelled:" + filter.isCancelled() +
                ", fixed_fee:" + filter.isFixedFee() +
                ", pending_payment:" + filter.isPendingPayment() +
                ", payment_plan:" + filter.isPaymentPlan());
    }
}