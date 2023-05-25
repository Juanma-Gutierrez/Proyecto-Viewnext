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
        // Base de datos
        bdInit();
        // Cargamos los datos
        init();
    }

    public void bdInit() {
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        InvoiceDAO dao = db.invoiceDAO();
        InvoiceVORepository repo = new InvoiceVORepositoryImpl(dao);
        InvoiceVO invoiceVO = new InvoiceVO();

        initBBDD(repo);

        invoiceVO.setDescEstado("probando");
        invoiceVO.setImporteOrdenacion(125);
        repo.insertInvoiceVO(invoiceVO);
    }

    public void initBBDD(InvoiceVORepository repo) {

        int size = repo.getSize();
        Log.d("repo", "" + size);
        // Borrado inicial de la bbdd previa si el tamaño es diferente de 0
        if (size != 0) {
            Log.d("repo", "borro bbdd");
            repo.deleteAll();
            repo.resetID();
        }
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