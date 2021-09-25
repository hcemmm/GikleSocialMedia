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
import com.example.giklesocialmedia.Adapter.YorumAdapter;
import com.example.giklesocialmedia.Adapter.YorumGikAdapter;
import com.example.giklesocialmedia.Model.Gikle;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.Model.Yorum;
import com.example.giklesocialmedia.R;
import com.example.giklesocialmedia.YorumGikActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GikFragment extends Fragment {

    String gikid;

    private RecyclerView recyclerView;
    private GikAdapter gikAdapter;
    private List<Gikle> gikleList;

    private RecyclerView recyclerView_yorum;
    private YorumAdapter yorummAdapter;
    private List<Yorum> yorumList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gik, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("Oneriler", Context.MODE_PRIVATE);
        gikid = prefs.getString("gikid", "none");

        recyclerView = view.findViewById(R.id.fraggik_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        gikleList = new ArrayList<>();
        gikAdapter = new GikAdapter(getContext(), gikleList);
        recyclerView.setAdapter(gikAdapter);

        recyclerView_yorum = view.findViewById(R.id.fraggik_recycler_yorum);
        recyclerView_yorum.setHasFixedSize(true);
        LinearLayoutManager mmLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_yorum.setLayoutManager(mmLayoutManager);

        yorumList = new ArrayList<>();
        yorummAdapter = new YorumAdapter(getContext(), yorumList, gikid);
        recyclerView_yorum.setAdapter(yorummAdapter);


        okuPost();
        okuYorumlar();

        return view;
    }

    private void okuPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler").child(gikid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gikleList.clear();
                Gikle gikle = dataSnapshot.getValue(Gikle.class);
                gikleList.add(gikle);

                gikAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void okuYorumlar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gikid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                yorumList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Yorum yorum = snapshot.getValue(Yorum.class);
                    yorumList.add(yorum);
                }

                yorummAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}