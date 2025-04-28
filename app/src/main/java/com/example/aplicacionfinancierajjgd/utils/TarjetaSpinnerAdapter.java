package com.example.aplicacionfinancierajjgd.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class TarjetaSpinnerAdapter extends ArrayAdapter<Tarjeta> {

    private LayoutInflater inflater;

    public TarjetaSpinnerAdapter(@NonNull Context context, @NonNull List<Tarjeta> tarjetas) {
        super(context, android.R.layout.simple_spinner_item, tarjetas);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Vista cuando el Spinner está cerrado (solo muestra el nombre)
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Tarjeta tarjeta = getItem(position);
        if (tarjeta != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(tarjeta.getNombreTarjeta());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Vista desplegable (también solo muestra el nombre)
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        Tarjeta tarjeta = getItem(position);
        if (tarjeta != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(tarjeta.getNombreTarjeta());
        }

        return convertView;
    }
}