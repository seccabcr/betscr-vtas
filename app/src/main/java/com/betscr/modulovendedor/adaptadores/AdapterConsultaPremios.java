package com.betscr.modulovendedor.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betscr.modulovendedor.R;
import com.betscr.modulovendedor.modelo.VO.PremiosVO;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterConsultaPremios extends RecyclerView.Adapter<AdapterConsultaPremios.ViewHolderItemFac> implements View.OnClickListener {

    ArrayList<PremiosVO> listaPremios;
    private View.OnClickListener listener;

    public AdapterConsultaPremios(ArrayList<PremiosVO> listaPremios) {
        this.listaPremios = listaPremios;
    }


    @Override
    public ViewHolderItemFac onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_premios, null, false);
        view.setOnClickListener(this);
        return new ViewHolderItemFac(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItemFac holder, int position) {
        DecimalFormat fdecimal = new DecimalFormat("##,###,###");
        DecimalFormat fentero = new DecimalFormat("0000000000");
        int monJugadoNor = listaPremios.get(position).getMon_jugado();
        int monJugadoRev = listaPremios.get(position).getMon_jugado_rev();
        int monPremioNor = listaPremios.get(position).getMon_premio();
        int monPremioRev = listaPremios.get(position).getMon_premio_rev();
        int monTotalPremio = monPremioNor+monPremioRev;

        holder.tvNumTkt.setText("Numero Tiquete: "+fentero.format(listaPremios.get(position).getNum_tkt()));
        holder.tvTotalPremio.setText(fdecimal.format(monTotalPremio));
        holder.tvMontoJugado.setText(fdecimal.format(monJugadoNor));
        holder.tvMontoPremio.setText(fdecimal.format(monPremioNor));
        holder.tvMontoJugadoRev.setText(fdecimal.format(monJugadoRev));
        holder.tvMontoPremioRev.setText(fdecimal.format(monPremioRev));

        holder.tvNomCliente.setText("Ref: "+listaPremios.get(position).getReferencia());

    }

    @Override
    public int getItemCount() {
        return listaPremios.size();
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

        TextView tvNumTkt, tvMontoJugado, tvMontoPremio, tvNomCliente, tvTotalPremio, tvMontoJugadoRev, tvMontoPremioRev;

        public ViewHolderItemFac(View itemView) {
            super(itemView);
            tvNumTkt = itemView.findViewById(R.id.itemPremio_numTkt);
            tvTotalPremio = itemView.findViewById(R.id.itemPremio_totalPremio);
            tvMontoJugado = itemView.findViewById(R.id.itemPremio_montoJugado);
            tvMontoJugadoRev = itemView.findViewById(R.id.itemPremio_montoJugadoRev);
            tvMontoPremio = itemView.findViewById(R.id.itemPremio_montoPremio);
            tvMontoPremioRev = itemView.findViewById(R.id.itemPremio_montoPremioRev);
            tvNomCliente = itemView.findViewById(R.id.itemPremio_referencia);

        }
    }
}
