package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.KullanicilarFragment;
import com.example.giklesocialmedia.Fragment.MesajFragment;
import com.example.giklesocialmedia.Fragment.ProfilFragment;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Mesaj;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesajMainActivity  extends AppCompatActivity {

    CircleImageView main_pp;
    TextView main_kullaniciadi;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj_main);

        Toolbar toolbar = findViewById(R.id.mesajmain_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        main_pp = findViewById(R.id.mesajmain_pp);
        main_kullaniciadi = findViewById(R.id.mesajmain_kullaniciadi);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                main_kullaniciadi.setText(kullanici.getKullaniciadi());
                if (kullanici.getProfil_pp().equals("default")){
                    main_pp.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(main_pp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final TabLayout tabLayout = findViewById(R.id.mesajmain_tab_layout);
        final ViewPager viewPager = findViewById(R.id.mesajmain_view_pager);

        reference = FirebaseDatabase.getInstance().getReference("Mesajlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int okunmamismesaj = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mesaj mesaj = snapshot.getValue(Mesaj.class);
                    if (mesaj.getAlici().equals(firebaseUser.getUid()) && !mesaj.isIsgorulme()){
                        okunmamismesaj++;
                    }
                }

                if (okunmamismesaj == 0){
                    viewPagerAdapter.addFragment(new MesajFragment(), "Mesajlar");
                } else {
                    viewPagerAdapter.addFragment(new MesajFragment(), "("+okunmamismesaj+") Mesajlar");
                }

                viewPagerAdapter.addFragment(new KullanicilarFragment(), "Kullanıcılar");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());

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
        status("offline");
    }
}
