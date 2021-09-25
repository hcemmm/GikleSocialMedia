package com.example.giklesocialmedia.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.BottomShet;
import com.example.giklesocialmedia.Adapter.FotograflarimAdapter;
import com.example.giklesocialmedia.Adapter.GikAdapter;
import com.example.giklesocialmedia.HikayeActivity;
import com.example.giklesocialmedia.HikayeEkleActivity;
import com.example.giklesocialmedia.MesajActivity;
import com.example.giklesocialmedia.MesajMainActivity;
import com.example.giklesocialmedia.Model.Gikle;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.KullaniciDetay;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.ProfilDetayActivity;
import com.example.giklesocialmedia.ProfilDuzenActivity;
import com.example.giklesocialmedia.ProfilResimActivity;
import com.example.giklesocialmedia.R;
import com.example.giklesocialmedia.TakipcilerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilFragment extends Fragment {

    CircleImageView profilfrag_pp;
    ImageView  profilfrag_ayarlar,profilfrag_mesaj,profilfrag_mesajat;
    TextView profilfrag_posts,profilfrag_websitesi, profilfrag_takipciler, profilfrag_takipedilen, profilfrag_adsoyad, profilfrag_bio,profilfrag_gik,
            profilfrag_kullaniciadi,profilfrag_dahafazlasi;
    Button profilfrag_editprofil;

    private List<String> myKaydetler;


    private RecyclerView recyclerView;
    private FotograflarimAdapter fotograflarimAdapter;
    private List<Post> postList;

    private RecyclerView recyclerView_gikler;
    private GikAdapter gikAdapter;
    private List<Gikle> gikleList;

    private RecyclerView recyclerView_saves;
    private FotograflarimAdapter fotograflarimAdapter_kaydetler;
    private List<Post> postList_kaydetler;

    FirebaseUser firebaseUser;
    String profileid;

    ImageButton profilfrag_fotolarim, profilfrag_kaydedilen,profilfrag_gikler;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        SharedPreferences prefs = getContext().getSharedPreferences("Oneriler", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        profilfrag_pp = view.findViewById(R.id.profil_profilfoto);
        profilfrag_posts = view.findViewById(R.id.profil_posts);
        profilfrag_gik = view.findViewById(R.id.profil_gik);
        profilfrag_takipciler = view.findViewById(R.id.profil_takipci);
        profilfrag_takipedilen = view.findViewById(R.id.profil_takip);
        profilfrag_adsoyad = view.findViewById(R.id.profil_advesoyad);
        profilfrag_bio = view.findViewById(R.id.profil_bio);
         profilfrag_editprofil= view.findViewById(R.id.profil_editprofil);
        profilfrag_kullaniciadi = view.findViewById(R.id.profil_kullaniciadi);
        profilfrag_websitesi = view.findViewById(R.id.profil_websitesi);
        profilfrag_fotolarim = view.findViewById(R.id.profil_my_fotos);
        profilfrag_gikler = view.findViewById(R.id.profil_gikler);
        profilfrag_kaydedilen = view.findViewById(R.id.profil_saved_fotos);
        profilfrag_ayarlar = view.findViewById(R.id.profil_ayarlar);
        profilfrag_dahafazlasi = view.findViewById(R.id.profil_dahabilgi);
        profilfrag_mesaj= view.findViewById(R.id.profil_mesaj);
        profilfrag_mesajat= view.findViewById(R.id.profil_mesajat);

        recyclerView = view.findViewById(R.id.profil_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        fotograflarimAdapter = new FotograflarimAdapter(getContext(), postList);
        recyclerView.setAdapter(fotograflarimAdapter);

        recyclerView_saves = view.findViewById(R.id.profil_recycler_view_save);
        recyclerView_saves.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers = new GridLayoutManager(getContext(), 3);
        recyclerView_saves.setLayoutManager(mLayoutManagers);
        postList_kaydetler = new ArrayList<>();
        fotograflarimAdapter_kaydetler = new FotograflarimAdapter(getContext(), postList_kaydetler);
        recyclerView_saves.setAdapter(fotograflarimAdapter_kaydetler);

        recyclerView_gikler = view.findViewById(R.id.profil_recycler_view_gikler);
        recyclerView_gikler.setHasFixedSize(true);
        LinearLayoutManager m2linearLayoutManager = new LinearLayoutManager(getContext());
        m2linearLayoutManager.setReverseLayout(true);
        m2linearLayoutManager.setStackFromEnd(true);
        recyclerView_gikler.setLayoutManager(m2linearLayoutManager);
        gikleList = new ArrayList<>();
        gikAdapter = new GikAdapter(getContext(), gikleList);
        recyclerView_gikler.setAdapter(gikAdapter);

        kullaniciBilgi();
        kullaniciDetayBilgi();
        alTakipciler();
        alnrPosts();
        alnrGik();
        myFotolarim();
        myGikler();
        myKaydetler();

        if (profileid.equals(firebaseUser.getUid())){
            profilfrag_editprofil.setText("Profili Düzenle");
            profilfrag_mesajat.setVisibility(View.GONE);
        } else {
            kontrolTakip();
            profilfrag_kaydedilen.setVisibility(View.GONE);
            profilfrag_mesaj.setVisibility(View.GONE);
            profilfrag_ayarlar.setVisibility(View.GONE);
        }

        profilfrag_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilhikayefotograf();

            }
        });

        profilfrag_mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MesajMainActivity.class));
            }
        });

        profilfrag_mesajat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MesajActivity.class);
                intent.putExtra("userid", profileid);
                startActivity(intent);
            }
        });

        profilfrag_editprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = profilfrag_editprofil.getText().toString();

                if (btn.equals("Profili Düzenle")){

                   startActivity(new Intent(getContext(), ProfilDuzenActivity.class));

                } else if (btn.equals("takip")){
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("takipediyor").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profileid)
                            .child("takipciler").child(firebaseUser.getUid()).setValue(true);
                    ekleBildirimler();

                } else if (btn.equals("takipediyor")){

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("takipediyor").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profileid)
                            .child("takipciler").child(firebaseUser.getUid()).removeValue();

                }
            }

        });

        profilfrag_fotolarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_gikler.setVisibility(View.GONE);
                recyclerView_saves.setVisibility(View.GONE);

            }
        });
        profilfrag_gikler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            recyclerView_gikler.setVisibility(View.VISIBLE);
                recyclerView_saves.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });


        profilfrag_websitesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linkify.addLinks(profilfrag_websitesi, Linkify.WEB_URLS);
            }
        });
        profilfrag_kaydedilen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_saves.setVisibility(View.VISIBLE);
                recyclerView_gikler.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

            }
        });

        profilfrag_takipciler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TakipcilerActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "takipciler");
                startActivity(intent);
            }
        });

        profilfrag_takipedilen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TakipcilerActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "takipediyor");
                startActivity(intent);
            }
        });

        profilfrag_dahafazlasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ProfilDetayActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "Hakkında");
                startActivity(intent);



            }
        });

        profilfrag_ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomShet bottomShet = new BottomShet();
                bottomShet.show(getChildFragmentManager(),"TAG");

            }
        });


        return view;
    }

    private void profilhikayefotograf() {

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
                    Intent intent = new Intent(getContext(), HikayeActivity.class);
                    intent.putExtra("userid", profileid);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ProfilResimActivity.class);
                    intent.putExtra("id", profileid);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void ekleBildirimler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(profileid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bildirimkullaniciid", firebaseUser.getUid());
        hashMap.put("bildirimmetin", "Seni takip etmeye başladı.");
        hashMap.put("bildirimpostid", "");
        hashMap.put("bildirimpost", false);

        reference.push().setValue(hashMap);
    }



    private void kullaniciDetayBilgi(){

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("KullaniciDetay").child(profileid);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KullaniciDetay kullaniciDetay = dataSnapshot.getValue(KullaniciDetay.class);
                profilfrag_websitesi.setText(kullaniciDetay.getWebsitesi());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void kullaniciBilgi(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                KullaniciDetay kullaniciDetay = dataSnapshot.getValue(KullaniciDetay.class);

                Glide.with(getContext()).load(kullanici.getProfil_pp()).into(profilfrag_pp);
                    profilfrag_kullaniciadi.setText(kullanici.getKullaniciadi());
                profilfrag_websitesi.setText(kullaniciDetay.getWebsitesi());
                profilfrag_adsoyad.setText(kullanici.getAdvesoyad());
                profilfrag_bio.setText(kullanici.getBio());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

   private void kontrolTakip(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(firebaseUser.getUid()).child("takipediyor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()){
                    profilfrag_editprofil.setText("takipediyor");
                } else{
                    profilfrag_editprofil.setText("takip");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alTakipciler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(profileid).child("takipciler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profilfrag_takipciler.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Takip").child(profileid).child("takipediyor");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profilfrag_takipedilen.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void alnrPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPostpaylasan().equals(profileid)){
                        i++;
                    }
                }
                profilfrag_posts.setText(""+i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alnrGik(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gikle gikle = snapshot.getValue(Gikle.class);
                    if (gikle.getGikpaylasan().equals(profileid)){
                        i++;
                    }
                }
                profilfrag_gik.setText(""+i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void myGikler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gikleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gikle gikle = snapshot.getValue(Gikle.class);
                    if (gikle.getGikpaylasan().equals(profileid)){
                        gikleList.add(gikle);
                    }
                }
                Collections.reverse(gikleList);
                gikAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myFotolarim(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPostpaylasan().equals(profileid)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                fotograflarimAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myKaydetler(){
        myKaydetler = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kaydetler").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    myKaydetler.add(snapshot.getKey());
                }
                okuKaydetler();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void okuKaydetler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_kaydetler.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    for (String id : myKaydetler) {
                        if (post.getPostid().equals(id)) {
                            postList_kaydetler.add(post);
                        }
                    }
                }
                fotograflarimAdapter_kaydetler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}