package com.example.giklesocialmedia.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giklesocialmedia.Adapter.MesajKullaniciAdapter;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.MesajList;
import com.example.giklesocialmedia.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MesajFragment extends Fragment {

    private RecyclerView recyclerView;

    private MesajKullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullanici;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<MesajList> kullaniciList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesaj, container, false);

        recyclerView = view.findViewById(R.id.mesajfrag_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        kullaniciList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("MesajList").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullaniciList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MesajList mesajList = snapshot.getValue(MesajList.class);
                    kullaniciList.add(mesajList);
                }

                mesajList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }



    private void mesajList() {
        mKullanici = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Kullanicilar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mKullanici.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    for (MesajList mesajList : kullaniciList){
                        if (kullanici.getId().equals(mesajList.getId())){
                            mKullanici.add(kullanici);
                        }
                    }
                }
                kullaniciAdapter = new MesajKullaniciAdapter(getContext(), mKullanici, true);
                recyclerView.setAdapter(kullaniciAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}