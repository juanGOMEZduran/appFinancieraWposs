package com.example.aplicacionfinancierajjgd;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerTarjetaSeleccionada extends AppCompatActivity {

    TextView nombreTarjetaSelec, fechaExpTarjetaSeleccionada, cvvTarjetaSeleccionada, PanTarjeraSeleccionada, montoTarjetaSeleccionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_tarjeta_seleccionada);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle =getIntent().getExtras();
        //int idUsuario=bundle.getInt("idUsuario")
        String nombreTarjeta=bundle.getString("nombre_tarjeta");
        double monto = bundle.getDouble("monto_tarjeta", 0.0);
        String cvvTarjeta=bundle.getString("cvv_tarjeta");
        String panTarjeta=bundle.getString("pan_tarjeta");
        String expiracion_tarjeta=bundle.getString( "expiracion_tarjeta");
        String panSeparado=formatearNumeroTarjeta(panTarjeta);

        nombreTarjetaSelec=findViewById(R.id.nombreTarjetaSelec);
        fechaExpTarjetaSeleccionada=findViewById(R.id.fechaExpTarjetaSeleccionada);
        cvvTarjetaSeleccionada=findViewById(R.id.cvvTarjetaSeleccionada);
        PanTarjeraSeleccionada=findViewById(R.id.PanTarjeraSeleccionada);
        montoTarjetaSeleccionada=findViewById(R.id.montoTarjetaSeleccionada);

        nombreTarjetaSelec.setText(nombreTarjeta);
        fechaExpTarjetaSeleccionada.setText(expiracion_tarjeta);
        cvvTarjetaSeleccionada.setText(cvvTarjeta);
        PanTarjeraSeleccionada.setText(panSeparado);
        montoTarjetaSeleccionada.setText(String.format("$%,.2f", monto));

    }

    private String formatearNumeroTarjeta(String pan) {
        if (pan == null || pan.length() != 16) {
            return pan;
        }
        return pan.substring(0, 4) + " " + pan.substring(4, 8) + " " +
                pan.substring(8, 12) + " " + pan.substring(12);
    }

    public void volver(View v){
        finish();
    }
}