package com.example.giklesocialmedia.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.giklesocialmedia.Adapter.GikAdapter;
import com.example.giklesocialmedia.Adapter.HikayeAdapter;
import com.example.giklesocialmedia.Adapter.PostAdapter;
import com.example.giklesocialmedia.GikActivity;
import com.example.giklesocialmedia.Model.Gikle;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnasayfaFragment extends Fragment {

    private RecyclerView recyclerView_gonderi;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private RecyclerView recyclerView_metin;
    private GikAdapter gikAdapter;
    private List<Gikle> gikleList;

    private RecyclerView recyclerView_hikaye;
    private HikayeAdapter hikayeAdapter;
    private List<Hikaye> hikayeList;

    Switch as_Switch;
    ImageView anasayfa_gik;
    private List<String> takipcList;

    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anasayfa, container, false);

        anasayfa_gik = view.findViewById(R.id.anasayfa_gikle);


        recyclerView_metin = view.findViewById(R.id.anasayfa_recycler_view_metin);
        recyclerView_metin.setHasFixedSize(true);
        LinearLayoutManager m2linearLayoutManager = new LinearLayoutManager(getContext());
        m2linearLayoutManager.setReverseLayout(true);
        m2linearLayoutManager.setStackFromEnd(true);
        recyclerView_metin.setLayoutManager(m2linearLayoutManager);
        gikleList = new ArrayList<>();
        gikAdapter = new GikAdapter(getContext(), gikleList);
        recyclerView_metin.setAdapter(gikAdapter);

        recyclerView_gonderi = view.findViewById(R.id.anasayfa_recycler_view);
        recyclerView_gonderi.setHasFixedSize(true);
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
        mlinearLayoutManager.setReverseLayout(true);
        mlinearLayoutManager.setStackFromEnd(true);
        recyclerView_gonderi.setLayoutManager(mlinearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView_gonderi.setAdapter(postAdapter);

        recyclerView_hikaye = view.findViewById(R.id.anasayfa_recycler_story);
        recyclerView_hikaye.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_hikaye.setLayoutManager(linearLayoutManager);
        hikayeList = new ArrayList<>();
        hikayeAdapter = new HikayeAdapter(getContext(), hikayeList);
        recyclerView_hikaye.setAdapter(hikayeAdapter);

        progressBar = view.findViewById(R.id.anasayfa_progressbar);
        as_Switch = view.findViewById(R.id.anasayfa_switch);

        kontrolTakip();
        recyclerView_metin.setVisibility(View.GONE);
        anasayfa_gik.setVisibility(View.GONE);

 as_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
     @Override
     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
         if (buttonView.isChecked()){
             recyclerView_metin.setVisibility(View.VISIBLE);
             recyclerView_gonderi.setVisibility(View.GONE);
             anasayfa_gik.setVisibility(View.VISIBLE);

         }
         else {
             anasayfa_gik.setVisibility(View.GONE);
             recyclerView_metin.setVisibility(View.GONE);
             recyclerView_gonderi.setVisibility(View.VISIBLE);

         }
     }
 });

        anasayfa_gik.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

         startActivity(new Intent(getContext(), GikActivity.class));
     }
 });



        return view;

    }

    private void kontrolTakip(){
        takipcList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Takip")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("takipediyor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                takipcList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    takipcList.add(snapshot.getKey());
                }

                okuPostlar();
                okuHikaye();
                okuGikler();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void okuPostlar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for (String id : takipcList){
                        if (post.getPostpaylasan().equals(id)){
                            postList.add(post);
                        }
                    }
                }

                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void okuGikler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gikleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gikle gikle = snapshot.getValue(Gikle.class);
                    for (String id : takipcList){
                        if (gikle.getGikpaylasan().equals(id)){
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

    private void okuHikaye(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long timecurrent = System.currentTimeMillis();
                hikayeList.clear();
                hikayeList.add(new Hikaye("", 0, 0, "",
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id : takipcList) {
                    int hikayesayc = 0;
                    Hikaye hikaye = null;
                    for (DataSnapshot snapshot : dataSnapshot.child(id).getChildren()) {
                        hikaye = snapshot.getValue(Hikaye.class);
                        if (timecurrent > hikaye.getHikayetimestart() && timecurrent < hikaye.getHikayetimeend()) {
                            hikayesayc++;
                        }
                    }
                    if (hikayesayc > 0){
                        hikayeList.add(hikaye);
                    }
                }

                hikayeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}