package com.example.proyectoviewnext;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoviewnext.filter.Filter;
import com.example.proyectoviewnext.filter.FilterFragment;
import com.example.proyectoviewnext.apiservice.InvoiceApiService;
import com.example.proyectoviewnext.invoice.InvoiceVO;
import com.example.proyectoviewnext.invoice.InvoicesAdapter;
import com.example.proyectoviewnext.invoice.InvoicesList;
import com.example.proyectoviewnext.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnButtonClickListener {

    private Filter filter;                   // Filtro a aplicar
    private List<InvoiceVO> invoiceVOList; // Array donde se guardarán los elementos de la lista
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
        // Menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.invoices_list_title);
        setSupportActionBar(toolbar);

        adapter = new InvoicesAdapter(invoiceVOList);
        adapter.setItemOnClickListener(position -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.alert_title).setMessage(R.string.alert_info).setPositiveButton(R.string.close_button, (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        Call<InvoicesList> call = InvoiceApiService.getApiService().getInvoices();
        call.enqueue(new Callback<InvoicesList>() {
            @Override
            public void onResponse(Call<InvoicesList> call, Response<InvoicesList> response) {
                if (response.isSuccessful()) {
                    invoiceVOList = response.body().getFacturas();
                    Log.d("onResponse elements", "Size of elements => " + invoiceVOList.size());
                    adapter.setInvoicesList(invoiceVOList);
                    RecyclerView recyclerView = findViewById(R.id.list_view_invoices);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                    setMaxAmount();
                }
            }

            @Override
            public void onFailure(Call<InvoicesList> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void openFilterFragment() {
        View fragmentContainer = findViewById(R.id.filter_container);
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    public void closeFilterFragment() {
        Log.d("close", "closeFilterFragment");
        View fragmentContainer = findViewById(R.id.filter_container);
        fragmentContainer.setVisibility(View.GONE);
    }


    public void setMaxAmount() {
        if (invoiceVOList != null) {
            double max = Integer.MIN_VALUE;
            for (InvoiceVO i : invoiceVOList) {
                if (i.getImporteOrdenacion() > max)
                    max = i.getImporteOrdenacion();
            }
            // Redondea el importe máximo en porciones indicadas por AMOUNT_PORTION, en este caso 50
            int roundedMax = (int) (Math.floor((max + AppConstants.AMOUNT_PORTION) / AppConstants.AMOUNT_PORTION)) * AppConstants.AMOUNT_PORTION;
            filter.setMaxAmount(roundedMax);
            filter.setAmountSelected(roundedMax);
        }
    }

    /**
     * Diálogo al hacer click en los items
     *
     * @param v La vista donde se hizo click
     */
    // VER LA POSIBILIDAD DE CAMBIAR ESTE CÓDIGO A LISTENER
    public void onItemClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alert_title).setMessage(R.string.alert_info).setPositiveButton(R.string.close_button, (dialog, which) -> dialog.dismiss());
        alertDialog.create().show();
    }

    /**
     * Abre el FilterFragment
     *
     * @param menuItem Elemento del menú
     */
    public void openFilter(MenuItem menuItem) {
        // Crear una instancia del FilterFragment
        FilterFragment filterFragment = new FilterFragment(filter, invoiceVOList);
        // Obtener el FragmentManager
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, filterFragment).commit();
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
            LocalDate datePicker = LocalDate.of(year1, month1 + 1, dayOfMonth);
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
        SimpleDateFormat newFormat = new SimpleDateFormat(AppConstants.API_DATE_FORMAT, new Locale(AppConstants.API_DATE_LANGUAGE, AppConstants.API_DATE_COUNTRY));
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
    public void onButtonClicked(ArrayList<InvoiceVO> filteredInvoiceVOS) {
        adapter.setInvoicesList(filteredInvoiceVOS);
    }
}