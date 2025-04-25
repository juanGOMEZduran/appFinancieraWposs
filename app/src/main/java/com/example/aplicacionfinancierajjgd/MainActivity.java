package com.example.aplicacionfinancierajjgd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;
import com.example.aplicacionfinancierajjgd.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textRegister;
    private ImageButton botonVer;
    private EditText editTextContrasena, editTextCorreo;

    private SessionManager session;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // INICIALIZA SessionManager aquí (esto es lo que faltaba)
        session = new SessionManager(this);

        // Verificar si ya está logueado
        if (session.isLoggedIn()) {
            redirigirAHome();
            return;
        }


        textRegister=findViewById(R.id.textRegister);
        botonVer=findViewById(R.id.botonVerContra2);
        editTextContrasena=findViewById(R.id.editTextContrasena);
        editTextCorreo=findViewById(R.id.editTextCorreo);

        textRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


                //Toast.makeText(getApplicationContext(), "¡Te vas a registrar!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, activity_register.class);
                startActivity(i);
            }
        });

        botonVer.setOnClickListener(v -> {
            if(editTextContrasena.getInputType() ==(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ){
                //Toast.makeText(getApplicationContext(), "¡Te vas a registrar!", Toast.LENGTH_SHORT).show();

                editTextContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                botonVer.setImageResource(R.drawable.icon_visibilty);
            }else{
                editTextContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                botonVer.setImageResource(R.drawable.icon_visibility_off);
            }
            editTextContrasena.setSelection(editTextContrasena.length());


        });
    }
    public void iniciarSesion(View v){
        String email=editTextCorreo.getText().toString();
        String contrasena=editTextContrasena.getText().toString();
        boolean hayErrores = false;

        if(email.isEmpty()){
            editTextCorreo.setBackgroundResource(R.drawable.edittext_border_error);
            hayErrores=true;
            error();


        }else{
            if(!esCorreoValido(email)){
                editTextCorreo.setBackgroundResource(R.drawable.edittext_border_error);
                hayErrores=true;

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Correo invalido!", Snackbar.LENGTH_SHORT);
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();

            }else if(esCorreoValido(email)){
                editTextCorreo.setBackgroundResource(R.drawable.edittext_border);
            }


        }

        if(contrasena.isEmpty()){
            hayErrores=true;
            editTextContrasena.setBackgroundResource(R.drawable.edittext_border_error);
            error();

        }else{
            editTextContrasena.setBackgroundResource(R.drawable.edittext_border);

        }

        if (!hayErrores) {
            AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this);

            // Verificar credenciales con BCrypt

            boolean credencialesCorrectas = db.verificarCredenciales(email, contrasena);

            if (credencialesCorrectas) {
                Cursor cursor = db.obtenerDatosUsuario(email);

                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") int idUsuario = cursor.getInt(cursor.getColumnIndex("id_usuario"));

                    // Guardar sesión en SharedPreferences
                    session.createLoginSession(idUsuario, email);

                    // Guardar sesión en base de datos
                    db.guardarSesion(idUsuario, true);

                    // Redirigir a Home
                    redirigirAHome();
                }
                cursor.close();
            }
            else {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Correo o contraseña incorrectos!", Snackbar.LENGTH_SHORT);
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();
            }
        }

    }

    public boolean esCorreoValido(String correo) {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }

    private void redirigirAHome() {
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this);
        Cursor cursor = db.obtenerDatosUsuario(session.getUserEmail());

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int idUsuario = cursor.getInt(cursor.getColumnIndex("id_usuario"));
            @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            // ... obtener otros datos

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            intent.putExtra("nombre", nombre);
            // ... otros extras
            startActivity(intent);
            finish();
        }
        cursor.close();
    }

    public void  error(){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡El o los datos no puede estar vacía!", Snackbar.LENGTH_SHORT);
        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
        snackbar.show();
    }


}