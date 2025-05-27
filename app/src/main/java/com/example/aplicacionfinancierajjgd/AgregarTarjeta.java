package com.example.aplicacionfinancierajjgd;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;
import com.google.android.material.snackbar.Snackbar;

public class AgregarTarjeta extends AppCompatActivity {

    EditText editTextNombreTarjeta;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_tarjeta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextNombreTarjeta=findViewById(R.id.editTextNombreTarjeta);

        Bundle bundle =getIntent().getExtras();
        idUsuario=bundle.getInt("idUsuario");





    }



    public void crearTargeta(View view) {   // <---- Aquí la T mayúscula
        String nomTarjeta = editTextNombreTarjeta.getText().toString();
        if (nomTarjeta.isEmpty()) {
            editTextNombreTarjeta.setBackgroundResource(R.drawable.edittext_border_error);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡No puede estar vacio el nombre de la tarjeta!", Snackbar.LENGTH_SHORT);
            TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
            snackbar.show();
        } else {
            // Aquí puedes seguir agregando la lógica
            AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this);

            db.crearTarjetaNueva( idUsuario, nomTarjeta);
            Toast.makeText(getApplicationContext(), "¡Se agrego una nueva tarjeta !", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void volver(View v){
        finish();
    }





}

