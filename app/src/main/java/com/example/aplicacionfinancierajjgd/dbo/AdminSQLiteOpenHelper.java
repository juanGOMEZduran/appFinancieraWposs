package com.example.aplicacionfinancierajjgd.dbo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Random;


public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "aplicacion_finaciera";
    private static final int DATABASE_VERSION = 1;

    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (\n" +
                "    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    nombre TEXT NOT NULL,\n" +
                "    email TEXT NOT NULL UNIQUE,\n" +
                "    celular TEXT NOT NULL,\n" +
                "    cedula TEXT NOT NULL UNIQUE,\n" +
                "    contrasena TEXT NOT NULL,\n" +
                "    recordarme INTEGER DEFAULT 0 \n" +
                ");");
        db.execSQL("CREATE TABLE sesiones (\n" +
                "    id_sesion INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_usuario INTEGER NOT NULL,\n" +
                "    activa INTEGER DEFAULT 1,\n" +
                "    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)\n" +
                ");");

        db.execSQL("CREATE TABLE tarjetas (\n" +
                "    id_tarjeta INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    id_usuario INTEGER NOT NULL,\n" +
                "    nombre_tarjeta TEXT NOT NULL,\n" +
                "    pan TEXT NOT NULL UNIQUE, \n" +
                "    expiracion TEXT NOT NULL, \n" +
                "    cvv TEXT NOT NULL,\n" +
                "    saldo REAL NOT NULL DEFAULT 1000000,\n" +
                "    principal INTEGER DEFAULT 0, \n" +
                "    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)\n" +
                ");");

        db.execSQL("CREATE TABLE transferencias (\n" +
                "    id_transferencia INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    tarjeta_origen INTEGER NOT NULL,\n" +
                "    tarjeta_destino INTEGER NOT NULL,\n" +
                "    monto REAL NOT NULL,\n" +
                "    fecha TEXT DEFAULT CURRENT_TIMESTAMP,\n" +
                "    FOREIGN KEY (tarjeta_origen) REFERENCES tarjetas(id_tarjeta),\n" +
                "    FOREIGN KEY (tarjeta_destino) REFERENCES tarjetas(id_tarjeta)\n" +
                ");");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean verificarCedula(String cedula){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor=bd.rawQuery("SELECT id_usuario FROM usuarios WHERE cedula =?", new String[]{cedula});
        boolean existe = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Hay al menos un resultado, entonces la cédula existe
                existe = true;
            }
            cursor.close(); // ¡No olvides cerrar el cursor!
        }

        // Si existe es true, retornamos false porque ya hay un usuario con esa cédula
        return !existe;


    }

    public boolean verificarCorreo(String correo){
        SQLiteDatabase bd = this.getWritableDatabase();
        Cursor cursor=bd.rawQuery("SELECT id_usuario FROM usuarios WHERE email =?", new String[]{correo});
        boolean existe = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Hay al menos un resultado, entonces la cédula existe
                existe = true;
            }
            cursor.close(); // ¡No olvides cerrar el cursor!
        }

        // Si existe es true, retornamos false porque ya hay un usuario con esa cédula
        return !existe;


    }

    public Cursor consultarTarjetaPrincipal(int id_usuario) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener la tarjeta principal del usuario
        String query = "SELECT id_tarjeta, nombre_tarjeta, pan, expiracion, cvv, saldo, principal " +
                "FROM tarjetas " +
                "WHERE id_usuario = ? AND principal = 1 " +
                "LIMIT 1";

        return db.rawQuery(query, new String[]{String.valueOf(id_usuario)});
    }

    public boolean registrarUsuario(String nombre, String correo, String numeroCelular, String numeroCedula, String contrasena ){


        String contrasenaEncriptada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("email", correo);
        registro.put("celular", numeroCelular);
        registro.put("cedula", numeroCedula);
        registro.put("contrasena",contrasenaEncriptada);

        long result= bd.insert("usuarios", null, registro);

        if(result==-1) return false;

        Cursor cursor=bd.rawQuery("SELECT id_usuario FROM usuarios WHERE cedula =?", new String[]{numeroCedula});
        if(cursor.moveToFirst()){
            int id_usuario= cursor.getInt(0);
            crearTarjetaPrincipal(id_usuario);
        }else {
            System.out.println("error en crear el usuario");

        }
        cursor.close();




        return true;
    }

    public boolean verificarCredenciales(String cedulaOCorreo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Buscar usuario por correo (o cédula si prefieres)
        Cursor cursor = db.rawQuery("SELECT contrasena FROM usuarios WHERE email = ?",
                new String[]{cedulaOCorreo});

        if(cursor.moveToFirst()) {
            String contrasenaEncriptada = cursor.getString(0);
            cursor.close();
            return BCrypt.checkpw(contrasena, contrasenaEncriptada);
        }

        cursor.close();
        return false;
    }


    private void  crearTarjetaPrincipal(int id_usuario){

        String pan=generarNumeroTarjeta();
        String cvv=String.valueOf((int) (Math.random()*900+100));
        String expiracion=generarExpiracion();

        ContentValues tarjeta= new ContentValues();
        tarjeta.put("id_usuario", id_usuario);
        tarjeta.put("nombre_tarjeta", "PRINCIPAL");
        tarjeta.put("pan", pan);
        tarjeta.put("expiracion", expiracion);
        tarjeta.put("cvv", cvv);
        tarjeta.put("saldo", 5000000);
        tarjeta.put("principal", 1);

        SQLiteDatabase db=this.getWritableDatabase();
        db.insert("tarjetas", null, tarjeta);


    }

    private String generarNumeroTarjeta(){
        Random rand = new Random();
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            numero.append(rand.nextInt(10));
        }
        return numero.toString();
    }

    private String generarExpiracion() {
        int mes = (int)(Math.random() * 12 + 1);
        int anio = (int)(Math.random() * 6 + 27);
        return String.format("%02d/%02d", mes, anio);
    }

    public Cursor obtenerDatosUsuario(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id_usuario, nombre, email, celular, cedula FROM usuarios WHERE email = ?",
                new String[]{email});
    }

    public void guardarSesion(int idUsuario, boolean mantenerSesion) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Primero desactivar cualquier sesión previa
        db.execSQL("UPDATE sesiones SET activa = 0 WHERE id_usuario = " + idUsuario);

        // Crear nueva sesión
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("activa", 1);
        db.insert("sesiones", null, values);

        // Si el usuario quiere mantener la sesión
        if (mantenerSesion) {
            // Aquí podrías implementar lógica adicional para sesiones persistentes
        }
    }

    public int obtenerUsuarioActivo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_usuario FROM sesiones WHERE activa = 1 LIMIT 1", null);

        if (cursor.moveToFirst()) {
            int idUsuario = cursor.getInt(0);
            cursor.close();
            return idUsuario;
        }
        cursor.close();
        return -1; // No hay usuario activo
    }

    public void cerrarSesion(int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE sesiones SET activa = 0 WHERE id_usuario = " + idUsuario);
    }
}
