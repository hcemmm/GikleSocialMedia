package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Adapter.MesajAdapter;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Mesaj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
public class MesajActivity extends AppCompatActivity {

    CircleImageView mesajac_pp;
    TextView mesajac_adsoyad;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton mesajac_gonder;
    EditText mesajac_metin;

    MesajAdapter mesajAdapter;
    List<Mesaj> mesajList;
    ValueEventListener gorulmeListe;
    RecyclerView recyclerView;

    String kullaniciid;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        Toolbar toolbar = findViewById(R.id.mesajac_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MesajActivity.this, MesajMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        recyclerView = findViewById(R.id.mesajac_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mesajac_pp = findViewById(R.id.mesajac_pp);
        mesajac_adsoyad = findViewById(R.id.mesajac_adsoyad);
        mesajac_metin = findViewById(R.id.mesajac_metin);
        mesajac_gonder = findViewById(R.id.mesajac_gonder);

        intent = getIntent();
        kullaniciid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        mesajac_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mesajac_metin.getText().toString();
                if (!msg.equals("")){
                    gonderMesaj(fuser.getUid(), kullaniciid, msg);
                } else {
                    Toast.makeText(MesajActivity.this, "Boş mesaj gönderemezsiniz", Toast.LENGTH_SHORT).show();
                }
                mesajac_metin.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(kullaniciid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                mesajac_adsoyad.setText(kullanici.getAdvesoyad());
                if (kullanici.getProfil_pp().equals("default")){
                    mesajac_pp.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(mesajac_pp);
                }
                okuMesaj(fuser.getUid(),kullaniciid,kullanici.getProfil_pp());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gorMesaj(kullaniciid);

    }
    private void gorMesaj(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Mesajlar");
        gorulmeListe = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mesaj mesaj = snapshot.getValue(Mesaj.class);
                    if (mesaj.getAlici().equals(fuser.getUid()) && mesaj.getGonderen().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isgorulme", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gonderMesaj(String gonderen, final String alici, String mesaj){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("dd.MM.yy");
        final  String kayittarih = currentdate.format(callfordate.getTime());

        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm");
        final  String kayitsaat = currenttime.format(callfortime.getTime());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gonderen", gonderen);
        hashMap.put("alici", alici);
        hashMap.put("mesaj", mesaj);
        hashMap.put("tarih", kayittarih);
        hashMap.put("saat", kayitsaat);
        hashMap.put("isgorulme", false);

        reference.child("Mesajlar").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("MesajList")
                .child(fuser.getUid())
                .child(kullaniciid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(kullaniciid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("MesajList")
                .child(kullaniciid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());
     }

    private void okuMesaj(final String bnid, final String kullaniciid, final String resimurl){
        mesajList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Mesajlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mesajList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mesaj mesaj = snapshot.getValue(Mesaj.class);
                    if (mesaj.getAlici().equals(bnid) && mesaj.getGonderen().equals(kullaniciid) ||
                            mesaj.getAlici().equals(kullaniciid) && mesaj.getGonderen().equals(bnid)){
                        mesajList.add(mesaj);
                    }

                    mesajAdapter = new MesajAdapter(MesajActivity.this, mesajList, resimurl);
                    recyclerView.setAdapter(mesajAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("durum", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");


    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(gorulmeListe);
        status("offline");


    }
}