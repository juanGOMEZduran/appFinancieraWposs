package com.example.aplicacionfinancierajjgd;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;
import com.example.aplicacionfinancierajjgd.utils.Tarjeta;
import com.example.aplicacionfinancierajjgd.utils.TarjetaAdapter;
import com.example.aplicacionfinancierajjgd.utils.TarjetaSpinnerAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class EnviarPlata extends AppCompatActivity {
    private AdminSQLiteOpenHelper dbHelper;

   private Snackbar snackbar;
    private TextView textView;

    private TextView montoActualTarjeta;
    private EditText editTextPanDestino, editTextMensajeTransferencia, editTextmontoEnviar;
    private Spinner spinnerSeleccionTarjeta;
    String panOrigen;
    double montotarjetaSeleccionda;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enviar_plata);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerSeleccionTarjeta=findViewById(R.id.spinnerSeleccionTarjeta);
        editTextPanDestino=findViewById(R.id.editTextPanDestino);
        editTextMensajeTransferencia=findViewById(R.id.editTextMensajeTransferencia);
        editTextmontoEnviar=findViewById(R.id.editTextmontoEnviar);
        montoActualTarjeta=findViewById(R.id.montoActualTarjeta);

        dbHelper = new AdminSQLiteOpenHelper(this);
        Bundle bundle =getIntent().getExtras();
        int idUsuario=bundle.getInt("idUsuario");
        List<Tarjeta> tarjetas = dbHelper.obtenerTodasLasTarjetasPorUsuario(idUsuario);
        TarjetaSpinnerAdapter adapter = new TarjetaSpinnerAdapter(this, tarjetas);
        spinnerSeleccionTarjeta.setAdapter(adapter);
        spinnerSeleccionTarjeta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tarjeta tarjetaSeleccionada = (Tarjeta) parent.getItemAtPosition(position);

               // Toast.makeText(EnviarPlata.this,"TU SELECCIÓN ES: " + tarjetaSeleccionada.getPan(),Toast.LENGTH_SHORT).show();

                 panOrigen=tarjetaSeleccionada.getPan();
                 montotarjetaSeleccionda= tarjetaSeleccionada.getSaldo();
                 montoActualTarjeta.setText(String.format("$%,.2f",montotarjetaSeleccionda ));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });

    }
    public void RealizarEnvio(View v){

        String panDestino=editTextPanDestino.getText().toString();
        String mensajeTrans=editTextMensajeTransferencia.getText().toString();
        String monto=editTextmontoEnviar.getText().toString();

        boolean error=true;
        if(panOrigen.equals("")){
            error=false;
        }
        if(panDestino.isEmpty()){
            error();
            editTextPanDestino.setBackgroundResource(R.drawable.edittext_border_error);
            error=false;

        }else{
            editTextPanDestino.setBackgroundResource(R.drawable.edittext_border);
        }

        if(mensajeTrans.isEmpty()){
            error();
            error=false;
            editTextMensajeTransferencia.setBackgroundResource(R.drawable.edittext_border_error);
        }else{
            editTextMensajeTransferencia.setBackgroundResource(R.drawable.edittext_border);

        }

        if(monto.isEmpty()){
            error();
            error=false;
            editTextmontoEnviar.setBackgroundResource(R.drawable.edittext_border_error);
        }else {
            editTextmontoEnviar.setBackgroundResource(R.drawable.edittext_border);

        }

        boolean existe=dbHelper.existeTarjetaConPAN(panDestino);

        if(!existe){
            error=false;
             snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Error Depronto el PAN NO EXISTE!", Snackbar.LENGTH_SHORT);
             textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
            snackbar.show();

        }
        if(error==true){
            snackbar = Snackbar.make(findViewById(android.R.id.content), "¡CARGANDO...!", Snackbar.LENGTH_SHORT);
             textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.parseColor("#FFA500"));
            snackbar.show();

            double montoEnviar=Double.parseDouble(monto);

            if(montoEnviar>0){

                if(montoEnviar<montotarjetaSeleccionda){
                    boolean exitoTransaccion=dbHelper.enviarDinero( panOrigen, panDestino, mensajeTrans, montoEnviar);
                    if(exitoTransaccion){
                        Toast.makeText(EnviarPlata.this,"Transaccion Realizada. " ,Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Error inesperado en la transacción."+"\nSalga y intente de nuevo mas tarde", Snackbar.LENGTH_SHORT);
                        textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                        snackbar.show();
                    }
                }else{
                    snackbar = Snackbar.make(findViewById(android.R.id.content), "No tienes suficiente saldo para la transacción.", Snackbar.LENGTH_SHORT);
                    textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                    snackbar.show();
                    editTextmontoEnviar.setBackgroundResource(R.drawable.edittext_border_error);
                }

            }else {
                snackbar = Snackbar.make(findViewById(android.R.id.content), "¡El monto no puede ser menor a 0.", Snackbar.LENGTH_SHORT);
                textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();
                editTextmontoEnviar.setBackgroundResource(R.drawable.edittext_border_error);
            }

        }else{

        }


    }

    private void error(){
         snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Error en un Dato o esta  vacio!", Snackbar.LENGTH_SHORT);
         textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
        snackbar.show();
    }

    public void volver(View v){
        finish();
    }





}