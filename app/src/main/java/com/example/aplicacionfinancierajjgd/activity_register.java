package com.example.aplicacionfinancierajjgd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class activity_register extends AppCompatActivity  {

    private EditText editTextNombreRegister, editTextApellidoRegister, editTextCorreoRegister, editTextNumeroRegister, editTextNumeroCedula, editTextContrasenaRegister, editTextContrasenaRegisterDos ;

    private ImageButton botonVerContra, botonVerContra2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextNombreRegister=findViewById(R.id.editTextNombreRegister);
        editTextApellidoRegister=findViewById(R.id.editTextApellidoRegister);
        editTextCorreoRegister=findViewById(R.id.editTextCorreoRegister);
        editTextNumeroRegister=findViewById(R.id.editTextNumeroRegister);
        editTextNumeroCedula=findViewById(R.id.editTextNumeroCedula);
        editTextContrasenaRegister=findViewById(R.id.editTextContrasenaRegister);
        editTextContrasenaRegisterDos=findViewById(R.id.editTextContrasenaRegisterDos);
        botonVerContra=findViewById(R.id.botonVerContra);
        botonVerContra2=findViewById(R.id.botonVerContra2);

        botonVerContra.setOnClickListener(v -> {
            if(editTextContrasenaRegister.getInputType() ==(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                editTextContrasenaRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                botonVerContra.setImageResource(R.drawable.icon_visibilty);
            }else{
                editTextContrasenaRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                botonVerContra.setImageResource(R.drawable.icon_visibility_off);
            }

        });

        botonVerContra2.setOnClickListener(v -> {
            if(editTextContrasenaRegisterDos.getInputType() ==(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                editTextContrasenaRegisterDos.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                botonVerContra2.setImageResource(R.drawable.icon_visibilty);
            }else{
                editTextContrasenaRegisterDos.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                botonVerContra2.setImageResource(R.drawable.icon_visibility_off);
            }

        });

        //para notiticación
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Regístrate por favor!", Snackbar.LENGTH_SHORT);
        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.parseColor("#3c8544"));
        snackbar.show();
    }


    public void register(View v){
        String nombreRegister=editTextNombreRegister.getText().toString();
        String apellidoRegister=editTextApellidoRegister.getText().toString();
        String correoRegister= editTextCorreoRegister.getText().toString();
        String numeroRegister=editTextNumeroRegister.getText().toString();
        String numeroCedula=editTextNumeroCedula.getText().toString();
        String contrasena1=editTextContrasenaRegister.getText().toString();
        String contrasena2=editTextContrasenaRegisterDos.getText().toString();
        boolean hayErrores = false;

        if (nombreRegister.isEmpty()) {
            editTextNombreRegister.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        } else if (nombreRegister.length() < 4) {
            editTextNombreRegister.setBackgroundResource(R.drawable.edittext_border_error);
            mostrarSnackbarError("¡Error: nombre menor a 4 caracteres!");
            hayErrores = true;
        } else if (!contieneSoloLetrasYEspacios(nombreRegister)) {
            editTextNombreRegister.setBackgroundResource(R.drawable.edittext_border_error);
            mostrarSnackbarError("¡Error: el nombre solo puede contener letras y espacios!");
            hayErrores = true;
        } else {
            editTextNombreRegister.setBackgroundResource(R.drawable.edittext_border);
        }


        if (apellidoRegister.isEmpty()) {
            editTextApellidoRegister.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        } else if (apellidoRegister.length() < 4) {
            editTextApellidoRegister.setBackgroundResource(R.drawable.edittext_border_error);
            mostrarSnackbarError("¡Error: apellido menor a 4 caracteres!");
            hayErrores = true;
        } else if (!contieneSoloLetrasYEspacios(apellidoRegister)) {
            editTextApellidoRegister.setBackgroundResource(R.drawable.edittext_border_error);
            mostrarSnackbarError("¡Error: el apellido solo puede contener letras y espacios!");
            hayErrores = true;
        } else {
            editTextApellidoRegister.setBackgroundResource(R.drawable.edittext_border);
        }


        if(correoRegister.isEmpty()){
            editTextCorreoRegister.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        }else{
            if(!esCorreoValidoRegister(correoRegister)){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Correo invalido!", Snackbar.LENGTH_SHORT);
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();

                editTextCorreoRegister.setBackgroundResource(R.drawable.edittext_border_error);
                hayErrores = true;
            }else{
                editTextCorreoRegister.setBackgroundResource(R.drawable.edittext_border);
            }
        }

        if(numeroRegister.isEmpty()){
            editTextNumeroRegister.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        }else{
            if(numeroRegister.length()<10){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Numero de telefono menor a 10 digitos!", Snackbar.LENGTH_SHORT);
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();
                editTextNumeroRegister.setBackgroundResource(R.drawable.edittext_border_error);
                hayErrores = true;
            }else{
                editTextNumeroRegister.setBackgroundResource(R.drawable.edittext_border);
            }

        }

        if(numeroCedula.isEmpty() ){
            editTextNumeroCedula.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        }else{
            if(numeroCedula.length()<7 || numeroCedula.length()>10){
                editTextNumeroCedula.setBackgroundResource(R.drawable.edittext_border_error);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "La cedula menor a 7 o mayor a 10!", Snackbar.LENGTH_SHORT);
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();
                hayErrores = true;
            }else{
                editTextNumeroCedula.setBackgroundResource(R.drawable.edittext_border);
            }



        }

        if(contrasena1.isEmpty()){
            editTextContrasenaRegister.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        }else{
            if(contrasena1.length()<4){
                editTextContrasenaRegister.setBackgroundResource(R.drawable.edittext_border_error);
                editTextContrasenaRegisterDos.setBackgroundResource(R.drawable.edittext_border_error);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "La contraseña no puede ser menos de 4 digitos!", Snackbar.LENGTH_SHORT);
                TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                snackbar.show();
                hayErrores = true;

            }else{
                editTextContrasenaRegister.setBackgroundResource(R.drawable.edittext_border);
            }

        }

        if(contrasena2.isEmpty()){
            editTextContrasenaRegisterDos.setBackgroundResource(R.drawable.edittext_border_error);
            error();
            hayErrores = true;
        }else{
            editTextContrasenaRegisterDos.setBackgroundResource(R.drawable.edittext_border);
        }

        if(!contrasena1.equals(contrasena2)){
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡LAS CONTRASEÑAS NO COINCIDEN!", Snackbar.LENGTH_SHORT);
            TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
            snackbar.show();

            editTextContrasenaRegister.setBackgroundResource(R.drawable.edittext_border_error);
            editTextContrasenaRegisterDos.setBackgroundResource(R.drawable.edittext_border_error);
            hayErrores = true;
        }else{
            editTextContrasenaRegister.setBackgroundResource(R.drawable.edittext_border);
            editTextContrasenaRegisterDos.setBackgroundResource(R.drawable.edittext_border);
        }

// Si no hay errores, limpiar todos los campos
        if(!hayErrores){

            AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this);
            boolean noExisteCedula=db.verificarCedula(numeroCedula);
            boolean noExisteCorreo=db.verificarCorreo(correoRegister);



            if(noExisteCedula && noExisteCorreo){
                String nombreApellidoUsuario=String.format("%s %s",nombreRegister, apellidoRegister);
                boolean exito=db.registrarUsuario(nombreApellidoUsuario, correoRegister, numeroRegister,numeroCedula, contrasena1 );
                if(exito){

                    Toast.makeText(getApplicationContext(), "¡Te haz registrado correctamente, porfavor incia SESION !", Toast.LENGTH_SHORT).show();


                    editTextNombreRegister.setText("");
                    editTextApellidoRegister.setText("");
                    editTextCorreoRegister.setText("");
                    editTextNumeroRegister.setText("");
                    editTextNumeroCedula.setText("");
                    editTextContrasenaRegister.setText("");
                    editTextContrasenaRegisterDos.setText("");


                    finish();
                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Error a crear el usuario nuevo!", Snackbar.LENGTH_SHORT);
                    TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                    snackbar.show();
                }
            }else{
                if(!noExisteCedula){
                    editTextNumeroCedula.setBackgroundResource(R.drawable.edittext_border_error);

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Ya hay un usuario con esa cedula!", Snackbar.LENGTH_SHORT);
                    TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                    snackbar.show();
                }
                if(!noExisteCorreo){
                    editTextCorreoRegister.setBackgroundResource(R.drawable.edittext_border_error);

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡Ya hay un usuario con ese correo!", Snackbar.LENGTH_SHORT);
                    TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
                    snackbar.show();
                }



            }




        }

    }

    public boolean esCorreoValidoRegister(String correo) {
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }
    public void volverLogin(View view){
        finishAffinity();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void  error(){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "¡El o los datos no puede estar vacía!", Snackbar.LENGTH_SHORT);
        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
        snackbar.show();
    }

    private boolean contieneSoloLetrasYEspacios(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+");
    }


    private void mostrarSnackbarError(String mensaje) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_SHORT);
        TextView textView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
        snackbar.show();
    }

}