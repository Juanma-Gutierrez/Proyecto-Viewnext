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
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Filter filter;
    private RecyclerView recycler_view_list; // Vista donde se cargan las facturas
    private ArrayList<Invoice> invoice_list; // Array donde se guardarán los elementos de la lista


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices_list);
        filter = new Filter();
        // Cargamos los datos
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

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

    // Diálogo al hacer click en los items
    public void onItemClick(View v) {
        AlertDialog.Builder alert_dialog = new AlertDialog.Builder(this);
        alert_dialog
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_info)
                .setPositiveButton(R.string.close_button, (dialog, which) -> dialog.dismiss());
        alert_dialog.create().show();
    }

    public void openFilter(MenuItem menu_item) {
        Log.d("openFilter", "Open filter");
        Log.d("filter", "datefrom:" + filter.getDate_from() +
                ", dateuntil:" + filter.getDate_until() +
                ", maxamount:" + filter.getMax_amount() +
                ", paid:" + filter.isPaid() +
                ", cancelled:" + filter.isCancelled() +
                ", fixed_fee:" + filter.isFixed_fee() +
                ", pending_payment:" + filter.isPending_payment() +
                ", payment_plan:" + filter.isPayment_plan());
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
        Button date_button;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date new_date = calendar.getTime();
        Log.d("filter", String.format("calendar gettime: %s", calendar.getTime()));


        // Seleccionamos el botón pulsado
        int buttonId = view.getId();
        if (buttonId == R.id.date_from_button) {
            Toast.makeText(this, "From", Toast.LENGTH_SHORT).show();
            date_button = findViewById(R.id.date_from_button);
            filter.setDate_from_temp(new_date);
        } else {
            Toast.makeText(this, "until", Toast.LENGTH_SHORT).show();
            date_button = findViewById(R.id.date_until_button);
            filter.setDate_until_temp(new_date);
        }
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                Log.d("openCalendar", "Year: " + year + ", Month: " + month + ", Day: " + day);
                String date = dayOfMonth + "/" + month + "/" + year;
                Log.d("openCalendar Date", date);
                date_button.setText(date);
/*                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, --month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                Date new_date = calendar.getTime();
                Log.d("filter:", String.format("new dateeeee:%s", new_date));*/
            }
        }, year, month, day);
        dpd.show();
    }
}