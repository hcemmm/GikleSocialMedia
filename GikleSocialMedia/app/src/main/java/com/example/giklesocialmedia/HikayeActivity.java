package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HikayeActivity extends  AppCompatActivity implements StoriesProgressView.StoriesListener {

    int sayac = 0;
    long tutZaman = 0L;
    long limit = 500L;

    StoriesProgressView storiesProgressView;
    ImageView hikayeactvity_resim, hikayeactvity_hikayefoto;
    TextView hikayeactvity_kullaniciadi;

    LinearLayout hikayeactvity_gor;
    TextView hikayeactvity_goruntusayi;
    ImageView hikayeactvity_sil;

    List<String> resimler;
    List<String> hikayeids;
    String kullaniciid;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tutZaman = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - tutZaman;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hikaye);

        storiesProgressView = findViewById(R.id.hikayeact_hikaye);
        hikayeactvity_resim = findViewById(R.id.hikayeact_resim);
        hikayeactvity_hikayefoto = findViewById(R.id.hikayeact_hikayefoto);
        hikayeactvity_kullaniciadi = findViewById(R.id.hikayeact_kullaniciadi);

        hikayeactvity_gor = findViewById(R.id.hikayeact_gor);
        hikayeactvity_goruntusayi = findViewById(R.id.hikayeact_gormesayi);
        hikayeactvity_sil = findViewById(R.id.hikayeact_sil);

        hikayeactvity_gor.setVisibility(View.GONE);
        hikayeactvity_sil.setVisibility(View.GONE);

        kullaniciid = getIntent().getStringExtra("userid");


        if (kullaniciid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            hikayeactvity_gor.setVisibility(View.VISIBLE);
            hikayeactvity_sil.setVisibility(View.VISIBLE);
        }

        getHikaye(kullaniciid);
        kullaniciBilgi(kullaniciid);

        View reverse = findViewById(R.id.hikayeact_geri);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);


        View skip = findViewById(R.id.hikayeact_gec);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);

        hikayeactvity_gor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HikayeActivity.this, TakipcilerActivity.class);
                intent.putExtra("id", kullaniciid);
                intent.putExtra("hikayeid", hikayeids.get(sayac));
                intent.putExtra("title", "Goruntuleme");
                startActivity(intent);
            }
        });

        hikayeactvity_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye")
                        .child(kullaniciid).child(hikayeids.get(sayac));
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(HikayeActivity.this, "Silindi!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onNext() {
        Glide.with(getApplicationContext()).load(resimler.get(++sayac)).into(hikayeactvity_resim);

        ekleGoruntulenme(hikayeids.get(sayac));
        gorSayi(hikayeids.get(sayac));

    }

    @Override
    public void onPrev() {
        if ((sayac - 1) < 0) return;
        Glide.with(getApplicationContext()).load(resimler.get(--sayac)).into(hikayeactvity_resim);

        gorSayi(hikayeids.get(sayac));

    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    private void getHikaye(String kullaniciid){
        resimler = new ArrayList<>();
        hikayeids = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye")
                .child(kullaniciid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resimler.clear();
                hikayeids.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Hikaye hikaye = snapshot.getValue(Hikaye.class);
                    long timecurrent = System.currentTimeMillis();
                    if (timecurrent > hikaye.getHikayetimestart() && timecurrent < hikaye.getHikayetimeend()) {
                        resimler.add(hikaye.getHikayefotourl());
                        hikayeids.add(hikaye.getHikayeid());
                    }
                }

                storiesProgressView.setStoriesCount(resimler.size());
                storiesProgressView.setStoryDuration(6000L);
                storiesProgressView.setStoriesListener(HikayeActivity.this);
                storiesProgressView.startStories(sayac);

                Glide.with(getApplicationContext()).load(resimler.get(sayac)).into(hikayeactvity_resim);

                ekleGoruntulenme(hikayeids.get(sayac));
                gorSayi(hikayeids.get(sayac));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kullaniciBilgi(String kullaniciid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar")
                .child(kullaniciid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(hikayeactvity_hikayefoto);
                hikayeactvity_kullaniciadi.setText(kullanici.getKullaniciadi());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ekleGoruntulenme(String storyid){
        FirebaseDatabase.getInstance().getReference().child("Hikaye").child(kullaniciid)
                .child(storyid).child("Goruntuleme").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }

    private void gorSayi(String storyid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye")
                .child(kullaniciid).child(storyid).child("Goruntuleme");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hikayeactvity_goruntusayi.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}