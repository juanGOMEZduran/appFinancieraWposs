package com.example.aplicacionfinancierajjgd;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;
import com.example.aplicacionfinancierajjgd.utils.Transferencia;
import com.example.aplicacionfinancierajjgd.utils.TransferenciaAdapter;

import java.util.List;

public class HistorialTranferencias extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransferenciaAdapter adapter;
    private List<Transferencia> transferencias;
    private AdminSQLiteOpenHelper dbHelper;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_tranferencias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el ID del usuario del Intent
        Bundle bundle = getIntent().getExtras();
        idUsuario = bundle.getInt("idUsuario");

        // Inicializar la base de datos
        dbHelper = new AdminSQLiteOpenHelper(this);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.listaTrasferenciasView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener y mostrar las transferencias
        cargarTransferencias();
    }

    private void cargarTransferencias() {
        // Obtener transferencias de la base de datos
        transferencias = dbHelper.obtenerTransferenciasPorUsuario(idUsuario);

        // Configurar el adaptador con el ID del usuario actual
        adapter = new TransferenciaAdapter(this, transferencias, idUsuario);
        recyclerView.setAdapter(adapter);
    }

    public void volver(View v) {
        finish();
    }
}