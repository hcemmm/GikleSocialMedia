package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.AnasayfaFragment;
import com.example.giklesocialmedia.Fragment.AramaFragment;
import com.example.giklesocialmedia.Fragment.BildirimFragment;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.Model.Kullanici;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.giklesocialmedia.R.id.nav_profil;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigationpost;
    Fragment secilenfragment = null;
    ImageView anasayfa_mesaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anasayfa_mesaj = findViewById(R.id.anasayfa_gikle);
        bottom_navigationpost = findViewById(R.id.main_bottom_navigation_post);
        bottom_navigationpost.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("Oneriler", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    new ProfilFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    new AnasayfaFragment()).commit();
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {

                switch (item.getItemId()){
                    case R.id.nav_anasayfa:
                        secilenfragment = new AnasayfaFragment();
                        break;
                    case R.id.nav_arama:
                        secilenfragment = new AramaFragment();
                        break;
                    case R.id.nav_post:
                        secilenfragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        break;
                    case R.id.nav_bildirim:
                        secilenfragment = new BildirimFragment();
                        break;

                    case nav_profil:
                        SharedPreferences.Editor editor = getSharedPreferences("Oneriler", MODE_PRIVATE).edit();
                        editor.putString("profileid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        editor.apply();
                        secilenfragment = new ProfilFragment();
                        break;
                }
                if (secilenfragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                            secilenfragment).commit();
                }

                return true;
            };
}