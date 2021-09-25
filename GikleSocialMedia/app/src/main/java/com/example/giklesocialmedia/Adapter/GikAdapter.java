package com.example.giklesocialmedia.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.AnasayfaFragment;
import com.example.giklesocialmedia.Fragment.GikFragment;
import com.example.giklesocialmedia.Fragment.PostFragment;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.HikayeActivity;
import com.example.giklesocialmedia.MainActivity;
import com.example.giklesocialmedia.Model.Gikle;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.Model.Yorum;
import com.example.giklesocialmedia.R;
import com.example.giklesocialmedia.TakipcilerActivity;
import com.example.giklesocialmedia.YorumGikActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GikAdapter extends RecyclerView.Adapter<GikAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Gikle> gikleList;

    private FirebaseUser firebaseUser;

    public GikAdapter(Context context, List<Gikle> gikleList) {
        this.mContext = context;
        this.gikleList = gikleList;
    }

    @NonNull
    @Override
    public GikAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gik_sab, parent, false);
        return new GikAdapter.ImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GikAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Gikle gikle = gikleList.get(position);

        holder.gik_metin.setText("  "+gikle.getGikmetin());

        paylasanBilgi(holder.gik_pp, holder.gik_kullaniciadi, gikle.getGikpaylasan(),holder.gik_adsoyad);
        isBegen(gikle.getGikid(), holder.gik_begen);
        nrBegeniler(holder.gik_begeniler, gikle.getGikid());
        alYorums(gikle.getGikid(), holder.gik_yorumlar);
        isZaman(gikle.getGikid(), holder.gik_zaman);

        holder.gik_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                        if (sayac > 0) {
                            Intent intent = new Intent(mContext, HikayeActivity.class);
                            intent.putExtra("userid", gikle.getGikpaylasan());
                            mContext.startActivity(intent);
                        } else {
                            SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                            editor.putString("profileid", gikle.getGikpaylasan());
                            editor.apply();

                            ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                                    new ProfilFragment()).commit();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        holder.gik_adsoyad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", gikle.getGikpaylasan());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new ProfilFragment()).commit();
            }
        });

        holder.gik_kullaniciadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", gikle.getGikpaylasan());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new ProfilFragment()).commit();
            }
        });

        holder.gik_metin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                editor.putString("gikid", gikle.getGikid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new GikFragment()).commit();

            }
        });

        holder.gik_begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.gik_begen.getTag().equals("begen")) {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gikle.getGikid())
                            .child(firebaseUser.getUid()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("BegeniList").child("GikBegeni").child(firebaseUser.getUid())
                            .child(gikle.getGikid()).setValue(true);
                    ekleBildirimler(gikle.getGikpaylasan(),gikle.getGikid());

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gikle.getGikid())
                            .child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("BegeniList").child("GikBegeni").child(firebaseUser.getUid())
                            .child(gikle.getGikid()).removeValue();

                }
            }
        });



        holder.gik_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumGikActivity.class);
                intent.putExtra("gikid", gikle.getGikid());
                intent.putExtra("publisherid", gikle.getGikpaylasan());
                mContext.startActivity(intent);
            }
        });

        holder.gik_yorumlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumGikActivity.class);
                intent.putExtra("gikid", gikle.getGikid());
                intent.putExtra("publisherid", gikle.getGikpaylasan());
                mContext.startActivity(intent);
            }
        });

        holder.gik_daha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.postmenu_duzenle:
                                duzenleGik(gikle.getGikid());
                                return true;
                            case R.id.postmenu_sil:
                                final String id = gikle.getGikid();
                                FirebaseDatabase.getInstance().getReference("Gikler")
                                        .child(gikle.getGikid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    silBildirimler(id, firebaseUser.getUid());
                                                    Intent intent = new Intent(mContext, ProfilFragment.class);
                                                    mContext.startActivity(intent);

                                                }
                                            }
                                        });
                                return true;
                            case R.id.postmenu_bildir:
                                Toast.makeText(mContext, "Gönderi Bildirimini Aldık!", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if (!gikle.getGikpaylasan().equals(firebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.postmenu_duzenle).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.postmenu_sil).setVisible(false);
                }
                popupMenu.show();
            }
        });


        holder.gik_begeniler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TakipcilerActivity.class);
                intent.putExtra("id", gikle.getGikid());
                intent.putExtra("title", "begeniler");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return gikleList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView gik_pp,gik_begen,gik_yorum,gik_daha;
        public TextView gik_kullaniciadi,gik_adsoyad, gik_metin,gik_zaman,gik_begeniler,gik_yorumlar;

        public ImageViewHolder(View itemView) {
            super(itemView);

            gik_pp = itemView.findViewById(R.id.giksab_pp);
            gik_kullaniciadi = itemView.findViewById(R.id.giksab_kullaniciadi);
            gik_adsoyad = itemView.findViewById(R.id.giksab_adsoyad);
            gik_metin = itemView.findViewById(R.id.giksab_postmetin);
            gik_zaman = itemView.findViewById(R.id.giksab_zaman);

            gik_begen = itemView.findViewById(R.id.giksab_begen);
            gik_begeniler = itemView.findViewById(R.id.giksab_begeniler);
            gik_yorum = itemView.findViewById(R.id.giksab_yorum);
            gik_yorumlar = itemView.findViewById(R.id.giksab_yorumlar);
            gik_daha = itemView.findViewById(R.id.giksab_more);

        }
    }

    private void isZaman(String gikId, final TextView zaman){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Gikler").child(gikId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (zaman !=null){
                    zaman.setText(dataSnapshot.getValue(Gikle.class).getGikzaman());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alYorums(String gikId, final TextView gikmetin){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Yorumlar").child(gikId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gikmetin.setText(dataSnapshot.getChildrenCount()+" ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ekleBildirimler(String kullaniciadi, String gikId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciadi);


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bildirimkullaniciid", firebaseUser.getUid());
        hashMap.put("bildirimmetin", "Giki beğendi.");
        hashMap.put("bildirimpostid", gikId);
        hashMap.put("bildirimgik", true);
        hashMap.put("bildirimpost", false);

        reference.push().setValue(hashMap);

    }

    private void isBegen(final String gikId, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler").child(gikId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_kalp);
                    imageView.setTag("begenildi");
                } else{
                    imageView.setImageResource(R.drawable.ic_boskalp);
                    imageView.setTag("begen");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void nrBegeniler(final TextView begeni, String gikID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gikID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                begeni.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void paylasanBilgi(ImageView profil_foto,TextView kullaniciadi, String userid,TextView adsoyad){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                Glide.with(mContext).load(kullanici.getProfil_pp()).into(profil_foto);
                kullaniciadi.setText(kullanici.getKullaniciadi());
                adsoyad.setText(kullanici.getAdvesoyad());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void silBildirimler(final String gikID, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("bildirimpostid").getValue().equals(gikID)){
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(mContext, AnasayfaFragment.class);
                                        mContext.startActivity(intent);
                                        Toast.makeText(mContext, "Silindi!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void duzenleGik(final String gikId){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Gik Düzenle");

        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(gikId, editText);

        alertDialog.setPositiveButton("Düzenle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("gikmetin", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Gikler")
                                .child(gikId).updateChildren(hashMap);
                    }
                });
        alertDialog.setNegativeButton("İptal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();
    }

    private void getText(String gikId, final EditText text){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler")
                .child(gikId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getValue(Gikle.class).getGikmetin());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}