package com.example.proyectoviewnext.apiservice;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.proyectoviewnext.R;
import com.example.proyectoviewnext.App;

import java.io.IOException;
import java.io.InputStream;

import co.infinum.retromock.BodyFactory;

final class ResourceBodyFactory implements BodyFactory {

    @Override
    public InputStream create(final String input) throws IOException {
        Context context = App.getContext();
        Resources r = context.getResources();
        InputStream inputStream = r.openRawResource(R.raw.response);
        return inputStream;
    }
}