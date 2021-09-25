package com.example.giklesocialmedia.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.giklesocialmedia.Adapter.GikAdapter;
import com.example.giklesocialmedia.Model.Gikle;
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


public class GikBegeniFragment extends Fragment {

    private List<String> myBegeniler;


    private RecyclerView recyclerView_gikbegeni;
    private GikAdapter gikAdapter;
    private List<Gikle> gikleList;

    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gik_begeniragment, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView_gikbegeni = view.findViewById(R.id.gikbegeni_recycler_view);
        recyclerView_gikbegeni.setHasFixedSize(true);
        LinearLayoutManager m2linearLayoutManager = new LinearLayoutManager(getContext());
        m2linearLayoutManager.setReverseLayout(true);
        m2linearLayoutManager.setStackFromEnd(true);
        recyclerView_gikbegeni.setLayoutManager(m2linearLayoutManager);
        gikleList = new ArrayList<>();
        gikAdapter = new GikAdapter(getContext(), gikleList);
        recyclerView_gikbegeni.setAdapter(gikAdapter);

        myGikBegeni();


        return view;
    }


    private void myGikBegeni(){
        myBegeniler = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BegeniList").child("GikBegeni").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    myBegeniler.add(snapshot.getKey());
                }
                okuBegeniler();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void okuBegeniler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gikleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gikle gikle = snapshot.getValue(Gikle.class);

                    for (String id : myBegeniler) {
                        if (gikle.getGikid().equals(id)) {
                            gikleList.add(gikle);
                        }
                    }
                }
                gikAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}