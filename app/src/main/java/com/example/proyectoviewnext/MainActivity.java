package com.example.proyectoviewnext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler_view_list; // Vista donde se cargan las facturas
    ArrayList<Invoice> invoice_list; // Array donde se guardarán los elementos de la lista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices_list);

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
        Toast.makeText(this, "OpenFilter", Toast.LENGTH_SHORT).show();
    }
}