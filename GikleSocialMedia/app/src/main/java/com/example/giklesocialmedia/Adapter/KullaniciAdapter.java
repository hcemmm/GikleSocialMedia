package com.example.giklesocialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.MainActivity;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Kullanici>  kullaniciList;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public KullaniciAdapter(Context context, List<Kullanici> users, boolean isFragment){
        mContext = context;
        kullaniciList = users;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public KullaniciAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_sab, parent, false);
        return new KullaniciAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KullaniciAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Kullanici user = kullaniciList.get(position);

        holder.kullaniciadap_takipet.setVisibility(View.VISIBLE);
        istakip(user.getId(), holder.kullaniciadap_takipet);

        holder.kullaniciadap_kullaniciadi.setText(user.getKullaniciadi());
        holder.kullaniciadap_adsoyad.setText(user.getAdvesoyad());
        Glide.with(mContext).load(user.getProfil_pp()).into(holder.kullaniciadap_pp);

        if (user.getId().equals(firebaseUser.getUid())){
            holder.kullaniciadap_takipet.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFragment) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                            new ProfilFragment()).commit();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("publisherid", user.getId());
                    mContext.startActivity(intent);
                }
            }
        });


        holder.kullaniciadap_takipet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.kullaniciadap_takipet.getText().toString().equals("takip")) {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("takipediyor").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getId())
                            .child("takipciler").child(firebaseUser.getUid()).setValue(true);

                    ekleBildirimler(user.getId());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("takipediyor").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getId())
                            .child("takipciler").child(firebaseUser.getUid()).removeValue();
                }
            }

        });
    }


    private void ekleBildirimler(String kullaniciadi){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciadi);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bildirimkullaniciid", firebaseUser.getUid());
        hashMap.put("bildirimmetin", "Seni takip etmeye başladı.");
        hashMap.put("bildirimpostid", "");
        hashMap.put("bildirimpost", false);

        reference.push().setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return kullaniciList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView kullaniciadap_kullaniciadi;
        public TextView kullaniciadap_adsoyad;
        public CircleImageView kullaniciadap_pp;
        public Button kullaniciadap_takipet;
        public TextView kullaniciprof_dahabilgi;

        public ImageViewHolder(View itemView) {
            super(itemView);

            kullaniciadap_kullaniciadi = itemView.findViewById(R.id.kullanicisab_kullaniciadi);
            kullaniciadap_adsoyad = itemView.findViewById(R.id.kullanicisab_advesoyad);
            kullaniciadap_pp = itemView.findViewById(R.id.kullanicisab_pp);
            kullaniciadap_takipet = itemView.findViewById(R.id.kullanicisab_takipet);
            kullaniciprof_dahabilgi = itemView.findViewById(R.id.profil_dahabilgi);
        }
    }

    private void istakip(final String userid, final Button button){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(firebaseUser.getUid()).child("takipediyor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()){
                    button.setText("takipediyor");
                } else{
                    button.setText("takip");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}