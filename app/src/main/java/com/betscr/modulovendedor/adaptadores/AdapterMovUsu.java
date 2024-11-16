package com.betscr.modulovendedor.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.MovCtaUsuVO;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AdapterMovUsu extends RecyclerView.Adapter<AdapterMovUsu.ViewHolderItemFac> implements View.OnClickListener {

    ArrayList<MovCtaUsuVO> listaMov;
    private View.OnClickListener listener;

    public AdapterMovUsu(ArrayList<MovCtaUsuVO> listaMov) {
        this.listaMov = listaMov;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mov_cuenta, null, false);
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
            fecha = df.parse(listaMov.get(position).getFechaMov());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvFechaMov.setText(dfDMY.format(fecha));
        holder.tvDocRefe.setText(listaMov.get(position).getDocRefe());
        int mon = listaMov.get(position).getMontoMov();
        holder.tvMontoMov.setText(fdecimal.format(mon));
        if(mon<0){
            holder.tvMontoMov.setTextColor(Color.RED);
        }else{
            holder.tvMontoMov.setTextColor(Color.GRAY);
        }
        holder.tvDetMov.setText(listaMov.get(position).getDetMov());
    }

    @Override
    public int getItemCount() {
        return listaMov.size();
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

        TextView tvFechaMov, tvDocRefe, tvMontoMov, tvDetMov;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvFechaMov = itemView.findViewById(R.id.iMov_fecha);
            tvDocRefe= itemView.findViewById(R.id.iMov_numDoc);
            tvMontoMov= itemView.findViewById(R.id.iMov_monto);
            tvDetMov= itemView.findViewById(R.id.iMov_detalle);
        }
    }
}
