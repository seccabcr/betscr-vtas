package com.betscr.modulovendedor.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.LiqDiaSorteoVO;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterReporteLiqSorteos extends RecyclerView.Adapter<AdapterReporteLiqSorteos.ViewHolderItemFac> {

    ArrayList<LiqDiaSorteoVO> listaSorteos;

    public AdapterReporteLiqSorteos(ArrayList<LiqDiaSorteoVO> listaSorteos) {
        this.listaSorteos = listaSorteos;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_liq_sorteos, null, false);
        //view.setOnClickListener(this);
        return new ViewHolderItemFac(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItemFac holder, int position) {
        DecimalFormat fdecimal = new DecimalFormat("###,###,###");
        int monVenta = listaSorteos.get(position).getMon_venta();
        int monComision = listaSorteos.get(position).getMon_comision();
        int monPremio = listaSorteos.get(position).getMon_premio();
        int monNeto = monVenta-monComision-monPremio;


        holder.tvnomSorteo.setText(listaSorteos.get(position).getNomSorteo());
        holder.tvNumPremiado.setText(listaSorteos.get(position).getNumPremiado());
        holder.tvMonVenta.setText(fdecimal.format(monVenta));
        holder.tvMonComision.setText(fdecimal.format(monComision));
        holder.tvMonPremio.setText(fdecimal.format(monPremio));
        holder.tvMonNeto.setText(fdecimal.format(monNeto));
        if(monNeto<0){
            holder.tvMonNeto.setTextColor(Color.RED);
        }else{
            holder.tvMonNeto.setTextColor(Color.GRAY);
        }

    }

    @Override
    public int getItemCount() {
        return listaSorteos.size();
    }


    public static class ViewHolderItemFac extends RecyclerView.ViewHolder {

        TextView tvnomSorteo, tvNumPremiado, tvMonVenta, tvMonComision, tvMonPremio, tvMonNeto;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvnomSorteo = itemView.findViewById(R.id.irls_nomSorteo);
            tvNumPremiado = itemView.findViewById(R.id.irls_numPremiado);
            tvMonVenta = itemView.findViewById(R.id.irls_monVenta);
            tvMonComision = itemView.findViewById(R.id.irls_monComision);
            tvMonPremio = itemView.findViewById(R.id.irls_monPremio);
            tvMonNeto = itemView.findViewById(R.id.irls_monNeto);

        }
    }
}
