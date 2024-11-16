package com.betscr.modulovendedor.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.VentaDiariaTktVO;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterReporteVentaTKT extends RecyclerView.Adapter<AdapterReporteVentaTKT.ViewHolderItemFac> implements View.OnClickListener {

    ArrayList<VentaDiariaTktVO> listaTkts;
    private View.OnClickListener listener;

    public AdapterReporteVentaTKT(ArrayList<VentaDiariaTktVO> listaSorteos) {
        this.listaTkts = listaSorteos;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_venta_tkt, null, false);
        view.setOnClickListener(this);
        return new ViewHolderItemFac(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItemFac holder, int position) {
        DecimalFormat fdecimal = new DecimalFormat("##,###,###");
        DecimalFormat fentero = new DecimalFormat("0000000000");
        holder.tvNumTkt.setText("Tkt: "+fentero.format(listaTkts.get(position).getNum_tkt()));
        holder.tvMonto.setText("Monto; "+fdecimal.format(listaTkts.get(position).getMon_tkt()));
        holder.tvNomCliente.setText("Ref: "+listaTkts.get(position).getNom_cliente());

    }

    @Override
    public int getItemCount() {
        return listaTkts.size();
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

    public static class ViewHolderItemFac extends RecyclerView.ViewHolder {

        TextView tvNumTkt, tvMonto, tvNomCliente;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvNumTkt = itemView.findViewById(R.id.irp_numeroTkt);
            tvMonto = itemView.findViewById(R.id.irp_monto_tkt);
            tvNomCliente = itemView.findViewById(R.id.irp_nomCliente);

        }
    }
}
