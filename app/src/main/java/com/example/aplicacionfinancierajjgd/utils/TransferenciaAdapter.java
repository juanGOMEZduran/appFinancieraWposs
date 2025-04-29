package com.example.aplicacionfinancierajjgd.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacionfinancierajjgd.R;
import com.example.aplicacionfinancierajjgd.dbo.AdminSQLiteOpenHelper;

import java.util.List;

public class TransferenciaAdapter extends RecyclerView.Adapter<TransferenciaAdapter.TransferenciaViewHolder> {

    private Context context;
    private List<Transferencia> transferencias;
    private int idUsuarioActual; // ID del usuario en sesión
    private AdminSQLiteOpenHelper dbHelper;

    public TransferenciaAdapter(Context context, List<Transferencia> transferencias, int idUsuarioActual) {
        this.context = context;
        this.transferencias = transferencias;
        this.idUsuarioActual = idUsuarioActual;
        this.dbHelper = new AdminSQLiteOpenHelper(context);
    }

    @NonNull
    @Override
    public TransferenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_transferencia, parent, false);
        return new TransferenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferenciaViewHolder holder, int position) {
        Transferencia transferencia = transferencias.get(position);

        holder.tipoTrans.setText(transferencia.getTipoTransaccion());
        holder.panOrigen.setText(mascararPAN(transferencia.getPanOrigen()));
        holder.panDestino.setText(mascararPAN(transferencia.getPanDestino()));
        holder.fechaTrans.setText(transferencia.getFecha());
        holder.montoTrans.setText(String.format("$%,.2f", transferencia.getMonto()));


        boolean esEnviada = esTarjetaDelUsuario(transferencia.getPanOrigen());

        if (esEnviada) {

            holder.imagenTipoTrans.setImageResource(R.mipmap.transferir);
            holder.tipoTrans.setText("Transferencia enviada");
            holder.tipoTrans.setTextColor(context.getResources().getColor(R.color.verdeSecundario));


        } else {
            holder.imagenTipoTrans.setImageResource(R.mipmap.pago);

            holder.tipoTrans.setText("Transferencia recibida");
            holder.tipoTrans.setTextColor(context.getResources().getColor(R.color.error));

        }
    }

    @Override
    public int getItemCount() {
        return transferencias.size();
    }


    private boolean esTarjetaDelUsuario(String pan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM tarjetas WHERE pan = ? AND id_usuario = ?",
                new String[]{pan, String.valueOf(idUsuarioActual)});

        boolean pertenece = cursor.getCount() > 0;
        cursor.close();
        return pertenece;
    }

    public static class TransferenciaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenTipoTrans;
        TextView tipoTrans, panOrigen, panDestino, fechaTrans, montoTrans;

        public TransferenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenTipoTrans = itemView.findViewById(R.id.imagenTipoTrans);
            tipoTrans = itemView.findViewById(R.id.TipoTrans);
            panOrigen = itemView.findViewById(R.id.textViewPanOrigen);
            panDestino = itemView.findViewById(R.id.PanDestino);
            fechaTrans = itemView.findViewById(R.id.fechaTransaccion);
            montoTrans = itemView.findViewById(R.id.montoTransaccion);
        }
    }

    private String mascararPAN(String pan) {
        if (pan == null || pan.length() < 4) return pan;
        return "**** **** **** " + pan.substring(pan.length() - 4);
    }
}