package com.example.giklesocialmedia.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.BottomShet;
import com.example.giklesocialmedia.Adapter.FotograflarimAdapter;
import com.example.giklesocialmedia.Adapter.GikAdapter;
import com.example.giklesocialmedia.HikayeActivity;
import com.example.giklesocialmedia.HikayeEkleActivity;
import com.example.giklesocialmedia.MesajActivity;
import com.example.giklesocialmedia.MesajMainActivity;
import com.example.giklesocialmedia.Model.Gikle;
import com.example.giklesocialmedia.Model.Hikaye;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.KullaniciDetay;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.ProfilDetayActivity;
import com.example.giklesocialmedia.ProfilDuzenActivity;
import com.example.giklesocialmedia.ProfilResimActivity;
import com.example.giklesocialmedia.R;
import com.example.giklesocialmedia.TakipcilerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GonderiBegeniFragment extends Fragment {

    private List<String> myBegeniler;

    private RecyclerView recyclerView_gonderibegeni;
    private FotograflarimAdapter fotograflarimAdapter_kaydetler;
    private List<Post> postList_kaydetler;

    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gonderi_begeni, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView_gonderibegeni = view.findViewById(R.id.gonderibegeni_recycler_view);
        recyclerView_gonderibegeni.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers = new GridLayoutManager(getContext(), 3);
        recyclerView_gonderibegeni.setLayoutManager(mLayoutManagers);
        postList_kaydetler = new ArrayList<>();
        fotograflarimAdapter_kaydetler = new FotograflarimAdapter(getContext(), postList_kaydetler);
        recyclerView_gonderibegeni.setAdapter(fotograflarimAdapter_kaydetler);

        myPostBegeni();


        return view;
    }


    private void myPostBegeni(){
        myBegeniler = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BegeniList").child("GonderiBegeni").child(firebaseUser.getUid());
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_kaydetler.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    for (String id : myBegeniler) {
                        if (post.getPostid().equals(id)) {
                            postList_kaydetler.add(post);
                        }
                    }
                }
                fotograflarimAdapter_kaydetler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}