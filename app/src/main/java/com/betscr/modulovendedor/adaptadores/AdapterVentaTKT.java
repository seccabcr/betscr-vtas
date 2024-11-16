package com.betscr.modulovendedor.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.VentaTktVO;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterVentaTKT extends RecyclerView.Adapter<AdapterVentaTKT.ViewHolderItemFac> implements View.OnClickListener {

    ArrayList<VentaTktVO> listaNumeros;
    private View.OnClickListener listener;

    public AdapterVentaTKT(ArrayList<VentaTktVO> listaSorteos) {
        this.listaNumeros = listaSorteos;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta_tkt, null, false);
        view.setOnClickListener(this);
        return new ViewHolderItemFac(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItemFac holder, int position) {
        DecimalFormat fdecimal = new DecimalFormat("####,###");
        DecimalFormat fentero = new DecimalFormat("###");
        holder.tvNumero.setText(listaNumeros.get(position).getNumero());
        holder.tvMonto.setText(fdecimal.format(listaNumeros.get(position).getMonto()));
        holder.tvMontoRev.setText(fdecimal.format(listaNumeros.get(position).getMontoRev()));

    }

    @Override
    public int getItemCount() {
        return listaNumeros.size();
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

        TextView tvNumero, tvMonto, tvMontoRev;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.item_tkt_numero);
            tvMonto= itemView.findViewById(R.id.item_tkt_monto);
            tvMontoRev= itemView.findViewById(R.id.item_tkt_monto_rev);
        }
    }
}
