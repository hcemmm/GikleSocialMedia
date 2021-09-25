package com.example.giklesocialmedia.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.GikFragment;
import com.example.giklesocialmedia.Fragment.PostFragment;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.Model.Bildirim;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Bildirim> mBildirim;


    public BildirimAdapter(Context context, List<Bildirim> notification){
        mContext = context;
        mBildirim = notification;
    }

    @NonNull
    @Override
    public BildirimAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bildirim_sab, parent, false);
        return new BildirimAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BildirimAdapter.ImageViewHolder holder, final int position) {

        final Bildirim bildirim = mBildirim.get(position);

        holder.bildirimadap_metin.setText(bildirim.getBildirimmetin());

        kullaniciBilgi(holder.bildirimadap_pp, holder.bildirimadap_kullaniciadi, bildirim.getBildirimkullaniciid());

        if (bildirim.isBildirimpost()) {
            holder.bildirimadap_postfoto.setVisibility(View.VISIBLE);
            postResim(holder.bildirimadap_postfoto, bildirim.getBildirimpostid());
        } else if(bildirim.isBildirimgik()) {
            holder.bildirimadap_postfoto.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bildirim.isBildirimpost()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", bildirim.getBildirimpostid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                            new PostFragment()).commit();
                }
              else if (bildirim.isBildirimgik()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                    editor.putString("gikid", bildirim.getBildirimpostid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                            new GikFragment()).commit();
                }
                else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", bildirim.getBildirimkullaniciid());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                            new ProfilFragment()).commit();
                }
            }
        });



    }
    @Override
    public int getItemCount() {
        return mBildirim.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView bildirimadap_pp, bildirimadap_postfoto;
        public TextView bildirimadap_kullaniciadi, bildirimadap_metin;

        public ImageViewHolder(View itemView) {
            super(itemView);

            bildirimadap_pp = itemView.findViewById(R.id.bildirimsab_pp);
            bildirimadap_postfoto = itemView.findViewById(R.id.bildirimsab_postfoto);
            bildirimadap_kullaniciadi = itemView.findViewById(R.id.bildirimsab_kullaniciadi);
            bildirimadap_metin = itemView.findViewById(R.id.bildirimsab_yorum);


        }
    }

    private void kullaniciBilgi(final ImageView imageView, final TextView bildirimadap_kullaniciadi, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Kullanicilar").child(publisherid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici user = dataSnapshot.getValue(Kullanici.class);
                Glide.with(mContext).load(user.getProfil_pp()).into(imageView);
                bildirimadap_kullaniciadi.setText(user.getKullaniciadi());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

   private void postResim(final ImageView bildirimadap_postfoto, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Postlar").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
              Glide.with(mContext).load(post.getPostiresim()).into(bildirimadap_postfoto);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}