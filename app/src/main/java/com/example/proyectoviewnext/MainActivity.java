package com.example.proyectoviewnext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

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

    public void init() {
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

        InvoicesAdapter adapter = new InvoicesAdapter(invoice_list);
        recycler_view_list.setAdapter(adapter);
    }
}