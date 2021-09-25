package com.example.giklesocialmedia.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giklesocialmedia.Adapter.BildirimAdapter;
import com.example.giklesocialmedia.Model.Bildirim;
import com.example.giklesocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BildirimFragment extends Fragment {

    private RecyclerView recyclerView;
    private BildirimAdapter bildirimAdapter;
    private List<Bildirim> bildirimLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bildirim, container, false);

        recyclerView = view.findViewById(R.id.bildirimfrag_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        bildirimLists = new ArrayList<>();
        bildirimAdapter = new BildirimAdapter(getContext(), bildirimLists);
        recyclerView.setAdapter(bildirimAdapter);

        okuBildirimler();

        return view;
    }

    private void okuBildirimler(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bildirimLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bildirim bildirim = snapshot.getValue(Bildirim.class);
                    bildirimLists.add(bildirim);
                }

                Collections.reverse(bildirimLists);
                bildirimAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}