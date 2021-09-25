package com.example.giklesocialmedia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.HikayeActivity;
import com.example.giklesocialmedia.HikayeEkleActivity;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HikayeAdapter extends  RecyclerView.Adapter<HikayeAdapter.ViewHolder>{

    private Context mContext;
    private List<Hikaye> mHikaye;



    public HikayeAdapter(Context mContext, List<Hikaye> mHikaye) {
        this.mContext = mContext;
        this.mHikaye = mHikaye;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.hikaye_ekle_sab, viewGroup, false);
            return new HikayeAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.hikaye_sab, viewGroup, false);
            return new HikayeAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Hikaye hikaye = mHikaye.get(i);

        bilgiKullanici(viewHolder, hikaye.getHikayekullaniciid(), i);

        if (viewHolder.getAdapterPosition() != 0) {
            gorHikaye(viewHolder, hikaye.getHikayekullaniciid());
        }

        if (viewHolder.getAdapterPosition() == 0){
            myHikayeler(viewHolder.hikayeadap_metin, viewHolder.hikayeadap_arti, false);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.getAdapterPosition() == 0){
                    myHikayeler(viewHolder.hikayeadap_metin, viewHolder.hikayeadap_foto, true);
                } else {
                    Intent intent = new Intent(mContext, HikayeActivity.class);
                    intent.putExtra("userid", hikaye.getHikayekullaniciid());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHikaye.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView hikayeadap_foto,hikayeadap_gor,hikayeadap_arti,hikayeadap_kullanicifoto;
        public TextView hikayeadap_kullaniciadi, hikayeadap_metin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hikayeadap_kullanicifoto = itemView.findViewById(R.id.hikayeekle_foto);
            hikayeadap_foto = itemView.findViewById(R.id.hikayesab_pp);
            hikayeadap_kullaniciadi = itemView.findViewById(R.id.hikayesab_kullaniciadi);
            hikayeadap_arti = itemView.findViewById(R.id.hikayeekle_arti);
            hikayeadap_metin = itemView.findViewById(R.id.hikayeekle_metin);
            hikayeadap_gor = itemView.findViewById(R.id.hikayesab_gor);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }
        return 1;
    }

    private void bilgiKullanici(final ViewHolder viewHolder, String userid, final int pos){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                if (pos != 0) {
                    Glide.with(mContext).load(kullanici.getProfil_pp()).into(viewHolder.hikayeadap_gor);
                    viewHolder.hikayeadap_kullaniciadi.setText(kullanici.getKullaniciadi());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myHikayeler(final TextView textView, final ImageView imageView, final boolean click){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sayac = 0;
                long timecurrent = System.currentTimeMillis();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Hikaye hikaye = snapshot.getValue(Hikaye.class);
                    if (timecurrent > hikaye.getHikayetimestart() && timecurrent < hikaye.getHikayetimeend()){
                        sayac++;
                    }
                }

                if (click) {
                    if (sayac > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hikaye izle",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, HikayeActivity.class);
                                        intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mContext.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Hikaye Ekle",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, HikayeEkleActivity.class);
                                        mContext.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    } else {
                        Intent intent = new Intent(mContext, HikayeEkleActivity.class);
                        mContext.startActivity(intent);
                    }
                } else {
                    if (sayac > 0){
                        textView.setText("Hikayelerim");
                        imageView.setVisibility(View.GONE);
                    } else {
                        textView.setText("Hikaye Ekle");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void gorHikaye(final ViewHolder viewHolder, String userid){
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Hikaye")
                .child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (!snapshot.child("Goruntuleme")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .exists() && System.currentTimeMillis() < snapshot.getValue(Hikaye.class).getHikayetimeend()){
                        i++;
                    }
                }

                if ( i > 0){
                    viewHolder.hikayeadap_foto.setVisibility(View.VISIBLE);
                    viewHolder.hikayeadap_gor.setVisibility(View.GONE);
                } else {
                    viewHolder.hikayeadap_foto.setVisibility(View.GONE);
                    viewHolder.hikayeadap_gor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}