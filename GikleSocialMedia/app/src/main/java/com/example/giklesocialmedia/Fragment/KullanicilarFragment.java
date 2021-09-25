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
import com.example.giklesocialmedia.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class KullanicilarFragment extends Fragment {

    private RecyclerView recyclerView;

    private MesajKullaniciAdapter mesajKullaniciAdapter;
    private List<Kullanici> kullaniciList;

    EditText kulfrag_arama;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kullanicilar, container, false);

        recyclerView = view.findViewById(R.id.kullanicilarfrag_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        kullaniciList = new ArrayList<>();

        okuKullanici();

        kulfrag_arama = view.findViewById(R.id.kullanicilarfrag_arama);
        kulfrag_arama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aramaKullanici(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void aramaKullanici(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Kullanicilar").orderByChild("kullaniciadi")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullaniciList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);

                    assert kullanici != null;
                    assert fuser != null;
                    if (!kullanici.getId().equals(fuser.getUid())){
                        kullaniciList.add(kullanici);
                    }
                }

                mesajKullaniciAdapter = new MesajKullaniciAdapter(getContext(), kullaniciList,false);
                recyclerView.setAdapter(mesajKullaniciAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void okuKullanici() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (kulfrag_arama.getText().toString().equals("")) {
                    kullaniciList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Kullanici kullanici = snapshot.getValue(Kullanici.class);

                        if (!kullanici.getId().equals(firebaseUser.getUid())) {
                            kullaniciList.add(kullanici);
                        }

                    }

                    mesajKullaniciAdapter = new MesajKullaniciAdapter(getContext(), kullaniciList,false);
                    recyclerView.setAdapter(mesajKullaniciAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}