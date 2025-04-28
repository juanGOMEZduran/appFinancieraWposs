package com.example.aplicacionfinancierajjgd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;
import com.example.aplicacionfinancierajjgd.utils.SessionManager;
import com.example.aplicacionfinancierajjgd.utils.Tarjeta;
import com.example.aplicacionfinancierajjgd.utils.TarjetaAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private SessionManager session;
    private AdminSQLiteOpenHelper dbHelper;
    private BottomNavigationView navegarHome;
    private TextView nombretargeta, panTarjeta, fechaExpi, cvvTarjeta, montoTarjeta;
    int idUsuario;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new AdminSQLiteOpenHelper(this);
        session = new SessionManager(this);
        nombretargeta=findViewById(R.id.nombretargetas);
        panTarjeta=findViewById(R.id.panTarjetas);
        fechaExpi=findViewById(R.id.fechasExpis);
        cvvTarjeta=findViewById(R.id.cvvTarjetas);
        montoTarjeta=findViewById(R.id.montoTarjetas);
        navegarHome=findViewById(R.id.navegarHome);







        // Verificar si no hay sesión
        if (!session.isLoggedIn() && getIntent().getExtras() == null) {
            logout();
            return;
        }




        Intent intent = getIntent();
         idUsuario = intent.getIntExtra("id_usuario", 0);
        String nombre = intent.getStringExtra("nombre");

        List<Tarjeta> tarjetas = dbHelper.obtenerTodasLasTarjetasPorUsuario(idUsuario);


        RecyclerView recyclerView = findViewById(R.id.lista_tarjetas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        TarjetaAdapter adapter = new TarjetaAdapter(tarjetas, tarjeta -> {

            Toast.makeText(getApplicationContext(), "¡Seleccionaste una tarjeta !", Toast.LENGTH_SHORT).show();



        });

        recyclerView.setAdapter(adapter);





        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setIcon(R.drawable.icon_person); // Asegúrate que este recurso existe
            getSupportActionBar().setTitle("   Bienvenido");
            getSupportActionBar().setSubtitle("   "+nombre);
        }

        Cursor cursor = dbHelper.consultarTarjetaPrincipal(idUsuario);
        if (cursor != null && cursor.moveToFirst()) {

            String nombreTarjeta = cursor.getString(1);
            String pan = cursor.getString(2);
            String panFormateado = formatearNumeroTarjeta(pan);
            String expiracion = cursor.getString(3);
            String cvv = cursor.getString(4);
            double saldo = cursor.getDouble(5);
            int principal = cursor.getInt(6);

            nombretargeta.setText(nombreTarjeta);
            panTarjeta.setText(panFormateado);
            fechaExpi.setText("Exp: "+expiracion);
            cvvTarjeta.setText("Cvv: "+cvv);
            montoTarjeta.setText(String.valueOf(saldo));


            cursor.close();
        } else {

        }


        navegarHome.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.itemHome) {

                    return true;
                } else if (id == R.id.itemTransacciones) {


                    return true;
                } else if (id == R.id.itemperfil) {
                    //Toast.makeText(getApplicationContext(), "¡Ir al perfil!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(HomeActivity.this, PerfilUser.class);
                    intent.putExtra("idUsuario", idUsuario);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Recargar tarjetas actualizadas
        List<Tarjeta> tarjetasActualizadas = dbHelper.obtenerTodasLasTarjetasPorUsuario(idUsuario);

        RecyclerView recyclerView = findViewById(R.id.lista_tarjetas);

        // Crea el listener
        TarjetaAdapter.OnItemClickListener listener = tarjeta -> {
            Toast.makeText(HomeActivity.this,
                    "Seleccionaste: " + tarjeta.getNombreTarjeta(),
                    Toast.LENGTH_SHORT).show();
        };

        // Usa el constructor correcto con ambos parámetros
        TarjetaAdapter adapter = new TarjetaAdapter(tarjetasActualizadas, listener);
        recyclerView.setAdapter(adapter);
    }

    private String formatearNumeroTarjeta(String pan) {
        if (pan == null || pan.length() != 16) {
            return pan; // Retorna el original si no es válido
        }

        // Usamos StringBuilder para mejor performance
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < pan.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(pan.charAt(i));
        }
        return formatted.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubienvenida, menu);

        return true;
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_salir){

            logout();
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }





    private void logout() {
        session.logoutUser();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void irAgregarTarjeta(View view) {  // ¡Añade el parámetro View!
        Intent intent = new Intent(HomeActivity.this, AgregarTarjeta.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }





}