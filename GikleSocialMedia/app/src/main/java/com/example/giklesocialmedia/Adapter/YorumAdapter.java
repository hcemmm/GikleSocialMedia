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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.MainActivity;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Post;
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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Yorum> mYorum;
    private String postid;

    private FirebaseUser firebaseUser;

    public YorumAdapter(Context context, List<Yorum> yorumlar, String postid){
        mContext = context;
        mYorum = yorumlar;
        this.postid = postid;
    }

    public YorumAdapter(Context context, List<Yorum> yorumList) {
    }


    @NonNull
    @Override
    public YorumAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_sab, parent, false);
        return new YorumAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final YorumAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Yorum yorumm = mYorum.get(position);

        holder.yorumadap_yorum.setText(yorumm.getYorumadap_yorum());
        alKullaniciBilgi(holder.yorumadap_pp, holder.yorumadap_kullaniciadi, yorumm.getYorumadap_paylasan());
        holder.yorumadap_zaman.setText(yorumm.getYorumadap_zaman());

        holder.yorumadap_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", yorumm.getYorumadap_paylasan());
                mContext.startActivity(intent);
            }
        });
        holder.yorumadap_kullaniciadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", yorumm.getYorumadap_paylasan());
                mContext.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (yorumm.getYorumadap_paylasan().equals(firebaseUser.getUid())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Silmek istiyor musunuz?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hay??r",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Evet",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference("Yorumlar")
                                            .child(postid).child(yorumm.getYorumadap_yorumid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(mContext, "Silindi!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mYorum.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView yorumadap_pp;
        public TextView yorumadap_kullaniciadi, yorumadap_yorum,yorumadap_zaman;

        public ImageViewHolder(View itemView) {
            super(itemView);

            yorumadap_pp = itemView.findViewById(R.id.yorumsab_pp);
            yorumadap_kullaniciadi = itemView.findViewById(R.id.yorumsab_kullaniciadi);
            yorumadap_yorum = itemView.findViewById(R.id.yorumsab_yorum);
            yorumadap_zaman = itemView.findViewById(R.id.yorumsab_zaman);
        }
    }

    private void alKullaniciBilgi(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Kullanicilar").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici user = dataSnapshot.getValue(Kullanici.class);
                Glide.with(mContext).load(user.getProfil_pp()).into(imageView);
                username.setText(user.getKullaniciadi());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}