package com.example.giklesocialmedia.Adapter;

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
import com.example.giklesocialmedia.MesajActivity;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Mesaj;
import com.example.giklesocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MesajKullaniciAdapter extends RecyclerView.Adapter<MesajKullaniciAdapter.ViewHolder> {

    private Context mContext;
    private List<Kullanici> mkullanici;
    private boolean ischat;

    String thsonMesaj;

    public MesajKullaniciAdapter(Context mContext, List<Kullanici> mkullanici,boolean ischat){
        this.mkullanici = mkullanici;
        this.mContext = mContext;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mesajkullanici_sab, parent, false);
        return new MesajKullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Kullanici kullanici = mkullanici.get(position);

        holder.mesajkadapter_adsoyad.setText(kullanici.getAdvesoyad());

        if (kullanici.getProfil_pp().equals("default")){
            holder.mesajkadapter_pp.setImageResource(R.drawable.ic_profil);
        } else {
            Glide.with(mContext).load(kullanici.getProfil_pp()).into(holder.mesajkadapter_pp);
        }

        if (ischat){
            sonMesaj(kullanici.getId(), holder.mesajkadapter_son_mesaj);
        } else {
            holder.mesajkadapter_son_mesaj.setVisibility(View.GONE);
        }

        if (ischat){
            if (kullanici.getDurum().equals("online")){
                holder.mesajkadapter_img_online.setVisibility(View.VISIBLE);
                holder.mesajkadapter_img_offline.setVisibility(View.GONE);
            } else {
                holder.mesajkadapter_img_online.setVisibility(View.GONE);
                holder.mesajkadapter_img_offline.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mesajkadapter_img_online.setVisibility(View.GONE);
            holder.mesajkadapter_img_offline.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MesajActivity.class);
                intent.putExtra("userid", kullanici.getId());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mkullanici.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mesajkadapter_adsoyad;
        public ImageView mesajkadapter_pp;
        private ImageView mesajkadapter_img_online;
        private ImageView mesajkadapter_img_offline;
        private TextView mesajkadapter_son_mesaj;

        public ViewHolder(View itemView) {
            super(itemView);

            mesajkadapter_adsoyad = itemView.findViewById(R.id.mesajkulsab_adsoyad);
            mesajkadapter_pp = itemView.findViewById(R.id.mesajkulsab_pp);
            mesajkadapter_img_online = itemView.findViewById(R.id.mesajkulsab_img_on);
            mesajkadapter_img_offline = itemView.findViewById(R.id.mesajkulsab_img_off);
            mesajkadapter_son_mesaj = itemView.findViewById(R.id.mesajkulsab_last_msg);
        }
    }

    private void sonMesaj(final String kullaniciid, final TextView son_mesaj){
        thsonMesaj = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mesajlar");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mesaj mesaj = snapshot.getValue(Mesaj.class);
                    if (firebaseUser != null && mesaj != null) {
                        if (mesaj.getAlici().equals(firebaseUser.getUid()) && mesaj.getGonderen().equals(kullaniciid) ||
                                mesaj.getAlici().equals(kullaniciid) && mesaj.getGonderen().equals(firebaseUser.getUid())) {
                            thsonMesaj = mesaj.getMesaj();
                        }
                    }
                }

                switch (thsonMesaj){
                    case  "default":
                        son_mesaj.setText("Mesaj Yok!");
                        break;

                    default:
                        son_mesaj.setText(thsonMesaj);
                        break;
                }

                thsonMesaj = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}