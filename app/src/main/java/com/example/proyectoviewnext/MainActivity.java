package com.example.proyectoviewnext;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoviewnext.database.AppDatabase;
import com.example.proyectoviewnext.database.dao.InvoiceDAO;
import com.example.proyectoviewnext.database.repository.InvoiceVORepository;
import com.example.proyectoviewnext.database.repository.InvoiceVORepositoryImpl;
import com.example.proyectoviewnext.filter.Filter;
import com.example.proyectoviewnext.filter.FilterFragment;
import com.example.proyectoviewnext.apiservice.InvoiceApiService;
import com.example.proyectoviewnext.invoice.InvoiceVO;
import com.example.proyectoviewnext.invoice.InvoicesAdapter;
import com.example.proyectoviewnext.invoice.InvoicesList;
import com.example.proyectoviewnext.utils.AppConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FilterFragment.OnButtonClickListener {

    private Filter filter;
    private List<InvoiceVO> invoiceVOList;
    private InvoicesAdapter adapter;
    private InvoiceVORepository invoicesDB;

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
        dbInit();
        init();
    }

    /**
     * Creación de la base de datos
     */
    public void dbInit() {
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        InvoiceDAO dao = db.invoiceDAO();
        invoicesDB = new InvoiceVORepositoryImpl(dao);
    }

    /**
     * Inicialización de la base de datos, eliminando los datos previos si los hubiera
     *
     * @param invoicesDB Repositorio con la BBDD
     */
    public void clearDataBase(@NonNull InvoiceVORepository invoicesDB) {
        invoicesDB.deleteAll();
        invoicesDB.resetID();
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
        // Carga del Toolbar
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
                    clearDataBase(invoicesDB);
                    fillDataBase(invoiceVOList);
                    setMaxAmount();
                }
            }

            private void fillDataBase(List<InvoiceVO> invoiceVOList) {
                for (InvoiceVO i : invoiceVOList)
                    invoicesDB.insertInvoiceVO(i);
            }

            @Override
            public void onFailure(Call<InvoicesList> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
        invoiceVOList = invoicesDB.getAllItems();
        adapter.setInvoicesList(invoiceVOList);
        RecyclerView recyclerView = findViewById(R.id.list_view_invoices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
        setMaxAmount();
    }


    /**
     * Calcula el máximo valor que hay que poner en el SeekBar.
     */
    public void setMaxAmount() {
        if (invoiceVOList != null) {
            double max = Integer.MIN_VALUE;
            for (InvoiceVO i : invoiceVOList) {
                if (i.getImporteOrdenacion() > max)
                    max = i.getImporteOrdenacion();
            }
            // Redondea el importe máximo en porciones indicadas por AMOUNT_PORTION, en este caso 5
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

        DatePickerDialog dpd = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            LocalDate datePicker = LocalDate.of(year, month + 1, dayOfMonth);
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
        }, buttonDate.getYear(), buttonDate.getMonthValue() - 1, buttonDate.getDayOfMonth());
        dpd.show();
    }

    @Override
    public void onButtonClicked(ArrayList<InvoiceVO> filteredInvoiceVOS) {
        adapter.setInvoicesList(filteredInvoiceVOS);
    }
}