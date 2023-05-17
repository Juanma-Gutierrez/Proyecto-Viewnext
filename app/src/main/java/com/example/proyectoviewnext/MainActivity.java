package com.example.proyectoviewnext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Filter filter;
    private RecyclerView recycler_view_list; // Vista donde se cargan las facturas
    private ArrayList<Invoice> invoice_list; // Array donde se guardarán los elementos de la lista

    /**
     * Este método se llama cuando se crea la activity por primera vez
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
    }

    /**
     * Este método se llama cuando se crea el menú de opciones por primera vez
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
        invoice_list.add(new Invoice("12/10/2020", "Pendiente", 50.23));
        invoice_list.add(new Invoice("10/10/2020", "Pendiente", 30.10));
        invoice_list.add(new Invoice("8/10/2020", "Pagado", 5.75));
        invoice_list.add(new Invoice("6/10/2020", "", 125.50));
        invoice_list.add(new Invoice("12/10/2020", "Pendiente", 50.23));
        invoice_list.add(new Invoice("10/10/2020", "Pendiente", 30.10));
        invoice_list.add(new Invoice("8/10/2020", "Pagado", 5.75));
        invoice_list.add(new Invoice("6/10/2020", "", 125.50));
        invoice_list.add(new Invoice("12/10/2020", "Pendiente", 50.23));
        invoice_list.add(new Invoice("10/10/2020", "Pendiente", 30.10));
        invoice_list.add(new Invoice("8/10/2020", "Pagado", 5.75));
        invoice_list.add(new Invoice("6/10/2020", "", 125.50));

        InvoicesAdapter adapter = new InvoicesAdapter(invoice_list);
        recycler_view_list.setAdapter(adapter);
    }

    /**
     * Diálogo al hacer click en los items
     *
     * @param v La vista donde se hizo click
     */
    public void onItemClick(View v) {
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
        Log.d("openFilter", "Open filter");
        myLog("Open Filter");
        // Crear una instancia del FilterFragment
        FilterFragment filterFragment = new FilterFragment(filter);

        // Obtener el FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, filterFragment)
                .commit();
    }

    public void closeFilter(MenuItem menu_item) {
        Log.d("closeFilter", "Close filter");
        // Cerrar el FragmentManager
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                .commit();
    }

    public void openCalendar(View view) {
        myLog("NUEVO CALENDARIO");
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
                filter.setDate_from_temp(date_picker);
                if (button_selected == "from") {
                    myLog("fechas FROM");
                    if (filter.getDate_until() == null && filter.getDate_until_temp() == null) {
                        date_button_until.setText(dateFormat(current_day));
                        filter.setDate_until_temp(current_day);
                    }
                    date_button_from.setText(dateFormat(date_picker));
                    filter.setDate_from_temp(date_picker);
                } else { // button until selected
                    myLog("fechas UNTIL");
                    if (filter.getDate_from() == null && filter.getDate_from_temp() == null) {
                        date_button_from.setText(dateFormat(date_picker));
                        filter.setDate_from_temp(date_picker);
                    }
                    date_button_until.setText(dateFormat(date_picker));
                    filter.setDate_until_temp(date_picker);
                }
            }
        }, year, month, day);
        dpd.show();
    }

    public String dateFormat(Date date) {
        SimpleDateFormat new_format = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        String new_date = new_format.format(date);
        return new_date;
    }

    public Date getNewDate(int year, int month, int dayOfMonth) {
        Calendar new_calendar = Calendar.getInstance();
        new_calendar.set(Calendar.YEAR, year);
        new_calendar.set(Calendar.MONTH, month);
        new_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date_picker = new_calendar.getTime();
        return date_picker;
    }

    /**
     * Log con información del filtro
     *
     * @param msg Mensaje personalizado
     */
    public void myLog(String msg) {
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
}