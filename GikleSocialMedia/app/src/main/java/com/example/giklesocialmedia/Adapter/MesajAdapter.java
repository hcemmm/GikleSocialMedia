package com.example.giklesocialmedia.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.MainActivity;
import com.example.giklesocialmedia.MesajActivity;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Mesaj;
import com.example.giklesocialmedia.Model.Yorum;
import com.example.giklesocialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MesajAdapter extends RecyclerView.Adapter<MesajAdapter.ViewHolder> {

    public static  final int MSG_TYPE_SOL = 0;
    public static  final int MSG_TYPE_SAG = 1;

    private Context mContext;
    private List<Mesaj> mMesaj;
    private String resimrul;

    Mesaj mesaj;

    FirebaseUser fuser;

    public MesajAdapter(Context mContext, List<Mesaj> mMesaj, String resimrul){
        this.mMesaj = mMesaj;
        this.mContext = mContext;
        this.resimrul = resimrul;
    }

    @NonNull
    @Override
    public MesajAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_SAG) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.mesaj_sab_sag, parent, false);
            return new MesajAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.mesaj_sab_sol, parent, false);
            return new MesajAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MesajAdapter.ViewHolder holder, int position) {

        mesaj = mMesaj.get(position);
        holder.mesajadap_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", mesaj.getGonderen());
                mContext.startActivity(intent);
            }
        });


        holder.mesajadap_gosmesaj.setText(mesaj.getMesaj());
        holder.mesajadap_saat.setText(mesaj.getSaat());
        holder.mesajadap_tarih.setText(mesaj.getTarih());

        if (resimrul.equals("")){
            holder.mesajadap_pp.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(resimrul).into(holder.mesajadap_pp);
        }

        if (position == mMesaj.size()-1){
            if (mesaj.isIsgorulme()){
                holder.mesajadap_goruldu.setVisibility(View.VISIBLE);
                holder.mesajadap_iletildi.setVisibility(View.GONE);
            } else {
                holder.mesajadap_iletildi.setVisibility(View.VISIBLE);
                holder.mesajadap_goruldu.setVisibility(View.GONE);
            }
        } else {
            holder.mesajadap_goruldu.setVisibility(View.GONE);
            holder.mesajadap_iletildi.setVisibility(View.GONE);
        }

    }



    @Override
    public int getItemCount() {
        return mMesaj.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mesajadap_gosmesaj;
        public ImageView mesajadap_pp,mesajadap_goruldu,mesajadap_iletildi;
        public TextView mesajadap_saat,mesajadap_tarih;

        public ViewHolder(View itemView) {
            super(itemView);

            mesajadap_gosmesaj = itemView.findViewById(R.id.mesajsab_mesaj);
            mesajadap_pp = itemView.findViewById(R.id.mesajsab_pp);
            mesajadap_goruldu = itemView.findViewById(R.id.mesajsab_goruldu);
            mesajadap_iletildi = itemView.findViewById(R.id.mesajsab_ilteildi);
            mesajadap_saat = itemView.findViewById(R.id.mesajsab_saat);
            mesajadap_tarih = itemView.findViewById(R.id.mesajsab_tarih);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mMesaj.get(position).getGonderen().equals(fuser.getUid())){
            return MSG_TYPE_SAG;
        } else {
            return MSG_TYPE_SOL;
        }
    }
}