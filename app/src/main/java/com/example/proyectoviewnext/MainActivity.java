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
import android.widget.DatePicker;

import com.example.proyectoviewnext.filter.Filter;
import com.example.proyectoviewnext.filter.FilterFragment;
import com.example.proyectoviewnext.invoice.Invoice;
import com.example.proyectoviewnext.invoice.InvoicesAdapter;
import com.example.proyectoviewnext.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnButtonClickListener {

    private Filter filter;
    private RecyclerView recycler_view_list; // Vista donde se cargan las facturas
    private ArrayList<Invoice> invoice_list; // Array donde se guardarán los elementos de la lista
    private InvoicesAdapter adapter;

    /**
     * Se llama cuando se crea la activity por primera vez
     *
     * @param savedInstanceState Si la activity está siendo recreada, este bundle contiene los datos
     *                           previos almacenados
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate()");
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
        Log.d("debug", "onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Inicializa la carga de datos de facturas
     */
    public void init() {
        Log.d("debug", "init()");
        // Menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.invoices_list_title);
        setSupportActionBar(toolbar);

        // Asignamos a la lista la vista list_view_invoices
        recycler_view_list = (RecyclerView) findViewById(R.id.list_view_invoices);
        if (recycler_view_list != null) {
            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recycler_view_list.setLayoutManager(llm);
        }

        invoice_list = new ArrayList<>();
        invoice_list.add(new Invoice("12/05/2023", "Pagada", 50.23));
        invoice_list.add(new Invoice("10/05/2023", "Cuota Fija", 30.10));
        invoice_list.add(new Invoice("08/05/2023", "Pagada", 5.75));
        invoice_list.add(new Invoice("06/05/2023", "Plan de pago", 125.50));
        invoice_list.add(new Invoice("05/05/2023", "Pendiente de pago", 50.23));
        invoice_list.add(new Invoice("01/04/2023", "Pagada", 30.10));
        invoice_list.add(new Invoice("30/04/2023", "Anulada", 5.75));
        invoice_list.add(new Invoice("22/04/2023", "Plan de pago", 125.50));
        invoice_list.add(new Invoice("12/04/2023", "", 50.23));
        invoice_list.add(new Invoice("10/04/2023", "", 30.10));
        invoice_list.add(new Invoice("08/04/2023", "", 5.75));
        invoice_list.add(new Invoice("06/04/2023", "", 184.99));

        adapter = new InvoicesAdapter(invoice_list);
        recycler_view_list.setAdapter(adapter);
    }

    public void setMaxAmount() {
        double max = Integer.MIN_VALUE;
        for (Invoice i : invoice_list) {
            if (i.getAmount() > max)
                max = i.getAmount();
        }
        // Redondea el importe máximo en porciones indicadas por AMOUNT_PORTION, en este caso 50
        int rounded_max = (int) (Math.floor((max + AppConstants.AMOUNT_PORTION) / AppConstants.AMOUNT_PORTION)) * AppConstants.AMOUNT_PORTION;
        Log.d("tester", "Máximo:" + rounded_max);
        filter.setMax_amount(rounded_max);
        filter.setAmount_selected(rounded_max);
    }

    /**
     * Diálogo al hacer click en los items
     *
     * @param v La vista donde se hizo click
     */
    //TODO VER LA POSIBILIDAD DE CAMBIAR ESTE CÓDIGO A LISTENER
    public void onItemClick(View v) {
        Log.d("debug", "onItemClick");
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
        alert_dialog
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_info)
                .setPositiveButton(R.string.close_button, (dialog, which) -> dialog.dismiss());
        alert_dialog
                .create()
                .show();
    }

    /**
     * Abre el FilterFragment
     *
     * @param menu_item Elemento del menú
     */
    public void openFilter(MenuItem menu_item) {
        Log.d("debug", "openFilter()");
        // Crear una instancia del FilterFragment
        FilterFragment filterFragment = new FilterFragment(filter, invoice_list);
        // Obtener el FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, filterFragment)
                .commit();
    }

    /**
     * Cierra el fragment filter
     *
     * @param menu_item
     */
    public void closeFilter(MenuItem menu_item) {
        Log.d("debug", "closeFilter()");
        // Cerrar el FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                .commit();
    }

    /**
     * Abre un calendario para capturar el día y almacenarlo en filter
     *
     * @param view Vista cargada
     */
    public void openCalendar(View view) {
        Log.d("debug", "openCalendar()");
        Button date_button_from;
        Button date_button_until;
        String button_selected;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date new_date = calendar.getTime();
        Calendar current_date = Calendar.getInstance();
        Date current_day = current_date.getTime();

        // Capturamos los botones
        date_button_from = findViewById(R.id.date_from_button);
        date_button_until = findViewById(R.id.date_until_button);

        // Seleccionamos el botón pulsado
        int buttonId = view.getId();
        button_selected = (buttonId == R.id.date_from_button) ? "from" : "until";

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date_picker = getNewDate(year, month, dayOfMonth);
                if (button_selected == "from") {
                    // Comprobar si hay que rellenar until
                    if (filter.getDate_until() == null && filter.getDate_until_temp() == null) {
                        date_button_until.setText(dateFormat(current_day));
                        filter.setDate_until_temp(current_day);
                    }
                    date_button_from.setText(dateFormat(date_picker));
                    filter.setDate_from_temp(date_picker);
                } else { // button until selected
                    date_button_until.setText(dateFormat(date_picker));
                    filter.setDate_until_temp(date_picker);
                }
            }
        }, year, month, day);
        dpd.show();
    }

    /**
     * Aplica el formato local a la fecha pasada por parámetro y la convierte en String
     *
     * @param date Fecha a aplicar el formato
     * @return String con la fecha convertida en texto en formato local
     */
    public String dateFormat(Date date) {
        SimpleDateFormat new_format = new SimpleDateFormat(AppConstants.API_DATE_FORMAT,
                new Locale(AppConstants.API_DATE_LANGUAGE,
                        AppConstants.API_DATE_COUNTRY));
        String new_date = new_format.format(date);
        return new_date;
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
        Calendar new_calendar = Calendar.getInstance();
        new_calendar.set(Calendar.YEAR, year);
        new_calendar.set(Calendar.MONTH, month);
        new_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = new_calendar.getTime();
        return date;
    }

    /**
     * Log con información del filtro
     *
     * @param msg Mensaje personalizado
     */
    public void myLog(String tag, String msg) {
        Log.d("debug " + tag, msg + " -> from:" + filter.getDate_from() +
                ", until:" + filter.getDate_until() +
                ", fromtemp:" + filter.getDate_from_temp() +
                ", untiltemp:" + filter.getDate_until_temp() +
                ", maxamount:" + filter.getAmount_selected() +
                ", paid:" + filter.isPaid() +
                ", cancelled:" + filter.isCancelled() +
                ", fixed_fee:" + filter.isFixed_fee() +
                ", pending_payment:" + filter.isPending_payment() +
                ", payment_plan:" + filter.isPayment_plan());
    }

    @Override
    public void onButtonClicked(ArrayList<Invoice> filtered_invoices) {
        Log.d("debug click", "Se ha clickado el botón onbuttonclicked");
        myLog("aplicandoFiltro", "lo que se ha clicado");
        adapter.setInvoices_list(filtered_invoices);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
        Log.d("debug", "Se ha clickado el botón onPointerCaptureChanged");
    }
}