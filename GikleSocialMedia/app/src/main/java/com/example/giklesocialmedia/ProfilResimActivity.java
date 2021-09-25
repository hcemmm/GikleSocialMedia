package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.Model.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.http.Url;

public class ProfilResimActivity extends AppCompatActivity {

    ImageView profilresim_pp,profilresim_kapat,profilresim_indir;

    String id;
    String title;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_resim);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profilresim_pp = findViewById(R.id.profilresim_fotograf);
        profilresim_kapat = findViewById(R.id.profilresim_kapat);
        profilresim_indir = findViewById(R.id.profilresim_more);


        profilresim_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profilresim_indir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(kullanici.getProfil_pp())));
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE);
                        request.setTitle("İndir");
                        request.setDescription("Fotoğraf indiriliyor....");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis() + ".jpg");
                        DownloadManager manager = (DownloadManager)getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);

                        Toast.makeText(getApplicationContext(), "İndiriliyor", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

        profilGetir();
    }

   private void profilGetir(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(profilresim_pp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}