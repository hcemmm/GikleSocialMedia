package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Model.Kullanici;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GikActivity extends AppCompatActivity {


    ImageView gikactivity_kapat,gikactivity_pp;
    TextView gikactivity_post,gikactivity_kullaniciadi;
    EditText gikactivity_aciklama;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gik);

        gikactivity_kapat = findViewById(R.id.gikactivity_kapat);
        gikactivity_post = findViewById(R.id.gikactivity_gonder);
        gikactivity_aciklama = findViewById(R.id.gikactivity_metin);
        gikactivity_pp = findViewById(R.id.gikactivity_pp);
        gikactivity_kullaniciadi = findViewById(R.id.gikactivity_kullaniciadi);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                gikactivity_kullaniciadi.setText(kullanici.getKullaniciadi());
                Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(gikactivity_pp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });


        gikactivity_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GikActivity.this, MainActivity.class));
                finish();
            }
        });

        gikactivity_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {if (gikactivity_aciklama.getText().toString().equals("")){
                Toast.makeText(GikActivity.this, "Boş Gik gönderemezsiniz!", Toast.LENGTH_SHORT).show();
            } else {
                yukleGik();
            }

            }
        });

    }


    private void yukleGik(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Gikleniyor...");
        pd.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gikler");

        String gikId = reference.push().getKey();

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
        hashMap.put("gikid", gikId);
        hashMap.put("gikmetin", gikactivity_aciklama.getText().toString());
        hashMap.put("gikpaylasan", firebaseUser.getUid());
        hashMap.put("gikzaman", time);

        pd.dismiss();


        reference.child(gikId).setValue(hashMap);
        gikactivity_aciklama.setText("");


        startActivity(new Intent(GikActivity.this, MainActivity.class));
        finish();
    }

}
