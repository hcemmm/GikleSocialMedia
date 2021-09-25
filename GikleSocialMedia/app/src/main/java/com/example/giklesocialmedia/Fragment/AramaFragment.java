package com.example.giklesocialmedia.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.giklesocialmedia.Adapter.KullaniciAdapter;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AramaFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> kullaniciList;

    EditText arama_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_arama,container,false);

        recyclerView = view.findViewById(R.id.aramafrag_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arama_bar = view.findViewById(R.id.aramafrag_arabar);

        kullaniciList = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(getContext(), kullaniciList, true);
        recyclerView.setAdapter(kullaniciAdapter);

        kullaniciOku();
        arama_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                kullaniciArama(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void kullaniciArama(String s){
        Query query = FirebaseDatabase.getInstance().getReference("Kullanicilar").orderByChild("kullaniciadi")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullaniciList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    kullaniciList.add(kullanici);
                }
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kullaniciOku(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(arama_bar.getText().toString().equals(""));{
                    kullaniciList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Kullanici kullanici = snapshot.getValue(Kullanici.class);
                        kullaniciList.add(kullanici);
                    }
                    kullaniciAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}