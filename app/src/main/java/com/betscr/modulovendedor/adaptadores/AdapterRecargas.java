package com.betscr.modulovendedor.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.MovCtaUsuVO;
import com.betscr.modulovendedor.modelo.VO.Recarga;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterRecargas extends RecyclerView.Adapter<AdapterRecargas.ViewHolderItemFac> implements View.OnClickListener {

    ArrayList<Recarga> listaRecargas;
    private View.OnClickListener listener;

    public AdapterRecargas(ArrayList<Recarga> lista) {
        this.listaRecargas = lista;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mov_recarga, null, false);
        view.setOnClickListener(this);
        return new ViewHolderItemFac(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItemFac holder, int position) {
        DecimalFormat fdecimal = new DecimalFormat("###,###,###");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dfDMY = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = df.parse(listaRecargas.get(position).getFec_recarga());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvNumRecarga.setText(String.valueOf(listaRecargas.get(position).getNum_recarga()));
        holder.tvFecRecarga.setText(dfDMY.format(fecha));
        holder.tvIdCliente.setText(listaRecargas.get(position).getId_cliente());
        int mon = listaRecargas.get(position).getMon_recarga();
        holder.tvMonto.setText(fdecimal.format(mon));

    }

    @Override
    public int getItemCount() {
        return listaRecargas.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }

    }

    public class ViewHolderItemFac extends RecyclerView.ViewHolder {

        TextView tvNumRecarga, tvFecRecarga, tvIdCliente, tvMonto;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvNumRecarga= itemView.findViewById(R.id.iRec_numero);
            tvFecRecarga = itemView.findViewById(R.id.iRec_fecha);
            tvIdCliente= itemView.findViewById(R.id.iRec_idCliente);
            tvMonto= itemView.findViewById(R.id.iRec_monto);

        }
    }
}
