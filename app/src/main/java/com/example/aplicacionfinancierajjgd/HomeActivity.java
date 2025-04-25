package com.example.aplicacionfinancierajjgd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicacionfinancierajjgd.utils.SessionManager;

public class HomeActivity extends AppCompatActivity {
    private SessionManager session;
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
        session = new SessionManager(this);

        // Verificar si no hay sesión
        if (!session.isLoggedIn()) {
            logout();
            return;
        }

        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("id_usuario", 0);
        String nombre = intent.getStringExtra("nombre");
        String email = intent.getStringExtra("email");
        String celular = intent.getStringExtra("celular");
        String cedula = intent.getStringExtra("cedula");

        // Ahora puedes usar estos datos como necesites
        TextView tvBienvenida = findViewById(R.id.tvBienvenida);
        tvBienvenida.setText("Bienvenido, " + nombre);
    }

    private void logout() {
        session.logoutUser();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cerrarSesion(View view) {
        logout();
    }
}