package com.example.giklesocialmedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.giklesocialmedia.Model.KullaniciDetay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.http.Url;

public class ProfilDetayActivity extends AppCompatActivity {


    TextView detay_isyeri,detay_ispozisyon,detay_faceookadres,
            detay_hakkimda,detay_twitteradres,detay_websitesi,detay_instagramadres,detay_yas,detay_telefon,detay_mail,detay_universite,detay_unibolum;

    String id;
    String title;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_detay);



        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.profildetay_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        detay_isyeri = findViewById(R.id.profildetay_isyeri);
        detay_ispozisyon = findViewById(R.id.profildetay_iskonumu);
        detay_faceookadres = findViewById(R.id.profildetay_facebook);
        detay_twitteradres = findViewById(R.id.profildetay_twitter);
        detay_instagramadres = findViewById(R.id.profildetay_instagram);
        detay_yas = findViewById(R.id.profildetay_yasi);
        detay_telefon= findViewById(R.id.profildetay_telefon);
        detay_mail = findViewById(R.id.profildetay_mail);
        detay_universite = findViewById(R.id.profildetay_universite);
        detay_unibolum = findViewById(R.id.profildetay_universitebolum);
        detay_hakkimda= findViewById(R.id.profildetay_hakkimda);
        detay_websitesi= findViewById(R.id.profildetay_websitesi);



        profilDetay();


    }

    private void profilDetay() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("KullaniciDetay").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KullaniciDetay kullaniciDetay = dataSnapshot.getValue(KullaniciDetay.class);

                detay_isyeri.setText(" "+kullaniciDetay.getIsyeri());
                detay_ispozisyon.setText(" "+kullaniciDetay.getIspozisyon());
                detay_faceookadres.setText(" "+kullaniciDetay.getFacebookadres());
                detay_twitteradres.setText(" "+kullaniciDetay.getTwitteradres());
                detay_instagramadres.setText(" "+kullaniciDetay.getInstagramadress());
                detay_telefon.setText(" "+kullaniciDetay.getTelefon());
                detay_mail.setText(" "+kullaniciDetay.getMailadres());
                detay_yas.setText(" "+kullaniciDetay.getYas());
                detay_universite.setText(" "+kullaniciDetay.getUniversiteadi());
                detay_unibolum.setText(" "+kullaniciDetay.getUniversitebolum());
                detay_websitesi.setText(" "+kullaniciDetay.getWebsitesi());
                detay_hakkimda.setText(kullaniciDetay.getHakkimda());


                detay_faceookadres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"+kullaniciDetay.getFacebookadres()));
                        startActivity(browserIntent);

                    }
                });
                detay_twitteradres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/"+kullaniciDetay.getTwitteradres()));
                        startActivity(browserIntent);
                    }
                });
                detay_instagramadres.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+kullaniciDetay.getInstagramadress()));
                        startActivity(browserIntent);
                    }
                });

                detay_websitesi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (detay_websitesi != null){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+kullaniciDetay.getWebsitesi()+"/"));
                            startActivity(browserIntent);
                        }else {
                            detay_websitesi.setVisibility(View.GONE);
                        }

                    }
                });

                detay_mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                        emailintent.setType("plain/text");
                        emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,kullaniciDetay.getMailadres());
                        startActivity(Intent.createChooser(emailintent, "Mail Gönder"));
                    }
                });
                detay_telefon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+detay_telefon.getText().toString()));
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}