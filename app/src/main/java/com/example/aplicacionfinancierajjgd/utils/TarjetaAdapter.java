package com.example.aplicacionfinancierajjgd.utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aplicacionfinancierajjgd.R;
import com.example.aplicacionfinancierajjgd.utils.Tarjeta;
import java.util.List;

public class TarjetaAdapter extends RecyclerView.Adapter<TarjetaAdapter.TarjetaViewHolder> {

    private List<Tarjeta> tarjetas;

    public TarjetaAdapter(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    @NonNull
    @Override
    public TarjetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_tarjetas, parent, false);
        return new TarjetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarjetaViewHolder holder, int position) {
        Tarjeta tarjeta = tarjetas.get(position);

        // Formatear PAN para mostrar solo los últimos 4 dígitos
       // String panFormateado = "•••• •••• •••• " + tarjeta.getPan().substring(12);
        String panFormateado =  tarjeta.getPan();
        double monto=tarjeta.getSaldo();
        String montoString=String.valueOf(monto);
        holder.nombreTarjeta.setText(tarjeta.getNombreTarjeta());
        holder.panTarjeta.setText(panFormateado);
        holder.fechaExpiracion.setText("Exp: " + tarjeta.getExpiracion());
        holder.cvvTarjeta.setText("CVV: " + tarjeta.getCvv());
        holder.saldoTarjeta.setText(montoString );
    }

    @Override
    public int getItemCount() {
        return tarjetas.size();
    }

    public static class TarjetaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTarjeta, panTarjeta, fechaExpiracion, cvvTarjeta, saldoTarjeta;

        public TarjetaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTarjeta = itemView.findViewById(R.id.nombretargetas);
            panTarjeta = itemView.findViewById(R.id.panTarjetas);
            fechaExpiracion = itemView.findViewById(R.id.fechasExpis);
            cvvTarjeta = itemView.findViewById(R.id.cvvTarjetas);
            saldoTarjeta = itemView.findViewById(R.id.montoTarjetas);
        }
    }
}