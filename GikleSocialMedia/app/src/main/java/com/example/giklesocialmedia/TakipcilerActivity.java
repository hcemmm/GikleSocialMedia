package com.example.giklesocialmedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.giklesocialmedia.Adapter.KullaniciAdapter;
import com.example.giklesocialmedia.Model.Kullanici;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TakipcilerActivity extends  AppCompatActivity {

    String id;
    String title;

    private List<String> idList;

    RecyclerView recyclerView;
    KullaniciAdapter kullaniciAdapter;
    List<Kullanici> kullaniciList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takipciler);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.takipciler_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.takipciler_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        kullaniciList = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(this, kullaniciList, false);
        recyclerView.setAdapter(kullaniciAdapter);

        idList = new ArrayList<>();


        switch (title) {
            case "begeniler":
                alBegeniler();
                break;
            case "takipediyor":
                alTakipler();
                break;
            case "takipciler":
                alTakipciler();
                break;
            case "Goruntuleme":
                getGoruntuleme();
                break;
        }

    }

    private void getGoruntuleme(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye")
                .child(id).child(getIntent().getStringExtra("hikayeid")).child("Goruntuleme");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                gosterKullanicilar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alTakipciler() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Takip")
                .child(id).child("takipciler");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                gosterKullanicilar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alTakipler() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Takip")
                .child(id).child("takipediyor");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                gosterKullanicilar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alBegeniler() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Begeniler")
                .child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                gosterKullanicilar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void gosterKullanicilar() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kullaniciList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    for (String id : idList){
                        if (kullanici.getId().equals(id)){
                            kullaniciList.add(kullanici);
                        }
                    }
                }
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}