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

public class YorumGikActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private YorumAdapter yorummAdapter;
    private List<Yorum> yorumList;


    EditText yorumgikac_yorumekle;
    ImageView yorumgikac_pp;
    TextView yorumgikac_post;

    String gikid;
    String paylasanid;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorum_gik);

        Toolbar toolbar = findViewById(R.id.yorumgikactivy_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.yorumgikactivy_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        yorumList = new ArrayList<>();
        yorummAdapter = new YorumAdapter(this, yorumList, gikid);
        recyclerView.setAdapter(yorummAdapter);

        yorumgikac_post = findViewById(R.id.yorumgikactivy_post);
        yorumgikac_yorumekle = findViewById(R.id.yorumgikactivy_yorumekle);
        yorumgikac_pp = findViewById(R.id.yorumgikactivy_pp);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        gikid = intent.getStringExtra("gikid");
        paylasanid = intent.getStringExtra("publisherid");

        yorumgikac_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yorumgikac_yorumekle.getText().toString().equals("")){
                    Toast.makeText(YorumGikActivity.this, "Boş yorum yapamazsınız!", Toast.LENGTH_SHORT).show();
                } else {
                    yorumekle();
                }
            }
        });
        alResim();
        okuYorumlar();
    }

    private void yorumekle(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gikid);

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
        hashMap.put("yorumadap_yorum", yorumgikac_yorumekle.getText().toString());
        hashMap.put("yorumadap_paylasan", firebaseUser.getUid());
        hashMap.put("yorumadap_yorumid", commentid);
        hashMap.put("yorumadap_zaman", time);

        reference.child(commentid).setValue(hashMap);
        ekleBildirimler();
        yorumgikac_yorumekle.setText("");

    }

    private void ekleBildirimler(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bildirimler").child(paylasanid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bildirimkullaniciid", firebaseUser.getUid());
        hashMap.put("bildirimmetin", "Gike yorum yaptı.: "+yorumgikac_yorumekle.getText().toString());
        hashMap.put("bildirimpostid", gikid);
        hashMap.put("bildirimgik", true);
        hashMap.put("bildirimpost", false);

        reference.push().setValue(hashMap);
    }


    private void alResim(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici user = dataSnapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(user.getProfil_pp()).into(yorumgikac_pp);
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