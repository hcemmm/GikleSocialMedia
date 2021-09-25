package com.example.giklesocialmedia.Adapter;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.giklesocialmedia.Fragment.AnasayfaFragment;
import com.example.giklesocialmedia.Fragment.PostFragment;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.HikayeActivity;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.ProfilResimActivity;
import com.example.giklesocialmedia.R;
import com.example.giklesocialmedia.TakipcilerActivity;
import com.example.giklesocialmedia.YorumActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> postList;



    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> postList) {
        this.mContext = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_sab,viewGroup,false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = postList.get(i);

        Glide.with(mContext).load(post.getPostiresim())
                .apply(new RequestOptions().placeholder(R.drawable.bosresim))
                .into(viewHolder.postadap_postfoot);

        if (post.getPostaciklama().equals("")){
            viewHolder.postadap_aciklama.setVisibility(View.GONE);
        }else {
            viewHolder.postadap_aciklama.setVisibility(View.VISIBLE);
            viewHolder.postadap_aciklama.setText(post.getPostaciklama());
        }

        paylasanBilgi(viewHolder.postadap_pp, viewHolder.postadap_adsoyad, viewHolder.postadap_paylasan, post.getPostpaylasan());
        isBegen(post.getPostid(), viewHolder.postadap_begen);
        nrBegeniler(viewHolder.postadap_begeniler, post.getPostid());
        alYorums(post.getPostid(), viewHolder.postadap_yorumlar);
        isKaydet(post.getPostid(), viewHolder.postadap_kaydet);
        isZaman(post.getPostid(), viewHolder.postadap_zaman);

        

        viewHolder.postadap_pp.setOnClickListener(new View.OnClickListener() {
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
                            intent.putExtra("userid", post.getPostpaylasan());
                            mContext.startActivity(intent);
                        } else {
                            SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                            editor.putString("profileid", post.getPostpaylasan());
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

        viewHolder.postadap_adsoyad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPostpaylasan());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new ProfilFragment()).commit();
            }
        });

        viewHolder.postadap_postfoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new PostFragment()).commit();
            }
        });

        viewHolder.postadap_paylasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPostid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new ProfilFragment()).commit();
            }
        });


        viewHolder.postadap_begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.postadap_begen.getTag().equals("begen")) {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(post.getPostid())
                            .child(firebaseUser.getUid()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("BegeniList").child("GonderiBegeni").child(firebaseUser.getUid())
                            .child(post.getPostid()).setValue(true);
                    ekleBildirimler(post.getPostpaylasan(),post.getPostid());

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(post.getPostid())
                            .child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("BegeniList").child("GonderiBegeni").child(firebaseUser.getUid())
                            .child(post.getPostid()).removeValue();
                }
            }
        });
        viewHolder.postadap_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPostpaylasan());
                mContext.startActivity(intent);
            }
        });

        viewHolder.postadap_yorumlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("publisherid", post.getPostpaylasan());
                mContext.startActivity(intent);
            }
        });
        viewHolder.postadap_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.postadap_kaydet.getTag().equals("kaydet")){
                    FirebaseDatabase.getInstance().getReference().child("Kaydetler").child(firebaseUser.getUid())
                            .child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Kaydetler").child(firebaseUser.getUid())
                            .child(post.getPostid()).removeValue();
                }
            }
        });


        viewHolder.postadap_begeniler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TakipcilerActivity.class);
                intent.putExtra("id", post.getPostid());
                intent.putExtra("title", "begeniler");
                mContext.startActivity(intent);
            }
        });

        viewHolder.postadap_daha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.postmenu_duzenle:
                                duzenlePost(post.getPostid());
                                return true;
                            case R.id.postmenu_sil:
                                final String id = post.getPostid();
                                FirebaseDatabase.getInstance().getReference("Postlar")
                                        .child(post.getPostid()).removeValue()
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
                if (!post.getPostpaylasan().equals(firebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.postmenu_duzenle).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.postmenu_sil).setVisible(false);
                }
                popupMenu.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView postadap_adsoyad, postadap_begeniler, postadap_paylasan, postadap_aciklama,postadap_yorumlar,postadap_zaman;
        public  ImageView postadap_pp, postadap_postfoot, postadap_begen, postadap_yorum, postadap_kaydet,postadap_daha;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postadap_pp = itemView.findViewById(R.id.sabpost_pp);
            postadap_adsoyad = itemView.findViewById(R.id.sabpost_adsoyad);
            postadap_postfoot = itemView.findViewById(R.id.sabpost_post_resim);
            postadap_begen = itemView.findViewById(R.id.sabpost_begen);
            postadap_yorum = itemView.findViewById(R.id.sabpost_yorum);
            postadap_kaydet = itemView.findViewById(R.id.sabpost_kaydet);
            postadap_begeniler = itemView.findViewById(R.id.sabpost_begeniler);
            postadap_paylasan = itemView.findViewById(R.id.sabpost_paylasan);
            postadap_aciklama = itemView.findViewById(R.id.sabpost_aciklama);
            postadap_yorumlar = itemView.findViewById(R.id.sabpost_yorumlar);
            postadap_daha = itemView.findViewById(R.id.sabpost_more);
            postadap_zaman = itemView.findViewById(R.id.sabpost_zaman);

        }
    }


    private void isZaman(String postId, final TextView zaman){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Postlar").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Post post = dataSnapshot.getValue(Post.class);
                zaman.setText(post.getPostzaman());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alYorums(String postId, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Yorumlar").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.setText(dataSnapshot.getChildrenCount()+" Yorumun tümünü gör");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ekleBildirimler(String kullaniciadi, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(kullaniciadi);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bildirimkullaniciid", firebaseUser.getUid());
        hashMap.put("bildirimmetin", "Senin gönderini beğendi.");
        hashMap.put("bildirimpostid", postid);
        hashMap.put("bildirimpost", true);
        hashMap.put("bildirimgik", false);

        reference.push().setValue(hashMap);
    }

    private void isBegen(final String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler").child(postid);
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

    private void nrBegeniler(final TextView likes, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Begeniler").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" beğeni");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void paylasanBilgi(ImageView profil_foto,TextView postadap_adsoyad, TextView paylasan, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                Glide.with(mContext).load(kullanici.getProfil_pp()).into(profil_foto);
                postadap_adsoyad.setText(kullanici.getAdvesoyad());
                paylasan.setText(kullanici.getKullaniciadi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void isKaydet(final String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Kaydetler").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.ic_dolukaydet);
                    imageView.setTag("kaydetti");
                } else{
                    imageView.setImageResource(R.drawable.ic_boskaydet);
                    imageView.setTag("kaydet");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void silBildirimler(final String postid, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("bildirimpostid").getValue().equals(postid)){
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

    private void duzenlePost(final String postid){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Post Düzenle");

        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(postid, editText);

        alertDialog.setPositiveButton("Düzenle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postaciklama", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Postlar")
                                .child(postid).updateChildren(hashMap);
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

    private void getText(String postid, final EditText editText){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar")
                .child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editText.setText(dataSnapshot.getValue(Post.class).getPostaciklama());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
