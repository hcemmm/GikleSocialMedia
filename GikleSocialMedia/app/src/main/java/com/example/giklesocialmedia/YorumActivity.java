package com.example.giklesocialmedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Adapter.YorumAdapter;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.Yorum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class YorumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private YorumAdapter yorummAdapter;
    private List<Yorum> yorumList;


    EditText yorumac_yorumekle;
    ImageView yorumac_pp;
    TextView yorumac_post;

    String postid;
    String paylasanid;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorum);

        Toolbar toolbar = findViewById(R.id.yorumactivy_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.yorumactivy_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        yorumList = new ArrayList<>();
        yorummAdapter = new YorumAdapter(this, yorumList, postid);
        recyclerView.setAdapter(yorummAdapter);

        yorumac_post = findViewById(R.id.yorumactivy_post);
        yorumac_yorumekle = findViewById(R.id.yorumactivy_yorumekle);
        yorumac_pp = findViewById(R.id.yorumactivy_pp);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        paylasanid = intent.getStringExtra("publisherid");

        yorumac_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yorumac_yorumekle.getText().toString().equals("")){
                    Toast.makeText(YorumActivity.this, "Boş yorum yapamazsınız!", Toast.LENGTH_SHORT).show();
                } else {
                    yorumekle();
                }
            }
        });
        alResim();
        okuYorumlar();
    }

    private void yorumekle(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Yorumlar").child(postid);

        String commentid = reference.push().getKey();

        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("dd.MM.yy");
        final  String savedate = currentdate.format(callfordate.getTime());

        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm");
        final  String savetime = currenttime.format(callfortime.getTime());
        String time = savetime+"\n"+savedate;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("yorumadap_yorum", yorumac_yorumekle.getText().toString());
        hashMap.put("yorumadap_paylasan", firebaseUser.getUid());
        hashMap.put("yorumadap_yorumid", commentid);
        hashMap.put("yorumadap_zaman", time);

        reference.child(commentid).setValue(hashMap);
        ekleBildirimler();
        yorumac_yorumekle.setText("");

    }

    private void ekleBildirimler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(paylasanid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bildirimkullaniciid", firebaseUser.getUid());
        hashMap.put("bildirimmetin", "Gönderine yorum yaptı:  "+yorumac_yorumekle.getText().toString());
        hashMap.put("bildirimpostid", postid);
        hashMap.put("bildirimpost", true);
        hashMap.put("bildirimgik", false);

        reference.push().setValue(hashMap);
    }


    private void alResim(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici user = dataSnapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(user.getProfil_pp()).into(yorumac_pp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void okuYorumlar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Yorumlar").child(postid);

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