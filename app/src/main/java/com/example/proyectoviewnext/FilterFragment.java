package com.example.proyectoviewnext;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilterFragment extends Fragment {
   public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_filter);
        toolbar.setTitle(R.string.filter_fragment_title);
        return view;
    }
}