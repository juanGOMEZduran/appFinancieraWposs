package com.example.aplicacionfinancierajjgd;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;

public class PerfilUser extends AppCompatActivity {

    private TextView nombrePerfil, correoPerfil, numeroPerfil, numeroCedulaPerfil;


    private AdminSQLiteOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_user);

        nombrePerfil=findViewById(R.id.nombrePerfil);
        correoPerfil=findViewById(R.id.correoPerfil);
        numeroPerfil=findViewById(R.id.numeroPerfil);
        numeroCedulaPerfil=findViewById(R.id.numeroCedulaPerfil);


        dbHelper = new AdminSQLiteOpenHelper(this);
        Bundle bundle =getIntent().getExtras();
        int idUsuario=bundle.getInt("idUsuario");



        Cursor cursor = dbHelper.consultarDatosPerfil(idUsuario);
        if (cursor != null && cursor.moveToFirst()) {

            String nombreUser = cursor.getString(0);
            String emailUser = cursor.getString(1);
            String Celular = cursor.getString(2);
            String cedula = cursor.getString(3);

            nombrePerfil.setText(nombreUser);
            correoPerfil.setText(emailUser);
            numeroPerfil.setText(Celular);
            numeroCedulaPerfil.setText(cedula);

            //Toast.makeText(getApplicationContext(), nombreUser, Toast.LENGTH_SHORT).show();







            cursor.close();
        }

       // Toast.makeText(getApplicationContext(), "el usuario es: "+idUsuario+".", Toast.LENGTH_SHORT).show();

    }

    public void volver(View v){
        finish();
    }
}