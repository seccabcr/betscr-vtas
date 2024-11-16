package com.betscr.modulovendedor.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.LiqDiaResumenVO;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterResumenLiqSorteos extends RecyclerView.Adapter<AdapterResumenLiqSorteos.ViewHolderItemFac> {

    ArrayList<LiqDiaResumenVO> listaSorteos;

    public AdapterResumenLiqSorteos(ArrayList<LiqDiaResumenVO> listaSorteos) {
        this.listaSorteos = listaSorteos;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resumen_liq_sorteos, null, false);
        return new ViewHolderItemFac(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItemFac holder, int position) {
        DecimalFormat fdecimal = new DecimalFormat("###,###,###");
        SimpleDateFormat formatoYMD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoDMY = new SimpleDateFormat("dd/MM/yyyy");

        int monVenta = listaSorteos.get(position).getMon_venta();
        int monComision = listaSorteos.get(position).getMon_comision();
        int monPremio = listaSorteos.get(position).getMon_premio();
        int monNeto = monVenta-monComision-monPremio;

        Date fechaJson = null;
        try {
            fechaJson = formatoYMD.parse(listaSorteos.get(position).getFecha_venta());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvFecha.setText(formatoDMY.format(fechaJson));
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

        TextView tvFecha, tvMonVenta, tvMonComision, tvMonPremio, tvMonNeto;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.iRes_fecha);
            tvMonVenta = itemView.findViewById(R.id.iRes_monVenta);
            tvMonComision = itemView.findViewById(R.id.iRes_monComision);
            tvMonPremio = itemView.findViewById(R.id.iRes_monPremio);
            tvMonNeto = itemView.findViewById(R.id.iRes_monNeto);

        }
    }
}
