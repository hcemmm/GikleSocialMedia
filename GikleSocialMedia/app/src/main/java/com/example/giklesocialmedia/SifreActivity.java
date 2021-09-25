package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Model.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.jvm.internal.Intrinsics;

public class SifreActivity extends AppCompatActivity {

    FirebaseAuth fire;
    CircleImageView sireac_pp;
    TextView sifreac_kaydet,sifreac_unuttum;
    ImageView sireac_kapat;
    MaterialEditText sifreac_mevcut,sifreac_yeni,sifreac_tekrar;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth auth;
    String kulid,mevcutsifre;
    FirebaseUser kul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre);

        sifreac_mevcut = findViewById(R.id.sifreac_mevcutsifre);
        sifreac_yeni = findViewById(R.id.sifreac_yenisifre);
        sifreac_tekrar = findViewById(R.id.sifreac_tekrarsifre);
        sifreac_kaydet = findViewById(R.id.sifreac_kaydet);
        sireac_pp = findViewById(R.id.sifreac_pp);
        sireac_kapat = findViewById(R.id.sifreac_kapat);
        sifreac_unuttum = findViewById(R.id.sifreac_unuttum);

        auth = FirebaseAuth.getInstance();

        kulid = auth.getCurrentUser().getUid();
        kul = auth.getCurrentUser();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        fire = FirebaseAuth.getInstance();

        sifreac_unuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SifreActivity.this, SifreResetActivity.class));
            }
        });

        sireac_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sifreac_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_mevcut = sifreac_mevcut.getText().toString();
                String str_yeni = sifreac_yeni.getText().toString();
                String str_tekrar = sifreac_tekrar.getText().toString();

                if (TextUtils.isEmpty(str_yeni) || TextUtils.isEmpty(str_mevcut) || TextUtils.isEmpty(str_tekrar)){
                    Toast.makeText(SifreActivity.this, "Tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                } else if(str_yeni.length() < 6 && str_tekrar.length() < 6 ){
                    Toast.makeText(SifreActivity.this, "Parolanız 6 karakterden uzun olmalıdır!", Toast.LENGTH_SHORT).show();
                } else if(!mevcutsifre.equals(str_mevcut)){
                    Toast.makeText(SifreActivity.this, "Mevcut Şifreniz hatalı!", Toast.LENGTH_SHORT).show();
                }else {
                    if (str_yeni.equals(str_tekrar)){

                        degistirsifre(str_yeni);
                    }
                    else {

                        Toast.makeText(SifreActivity.this, "Şifreleriniz uyuşmuyor!", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

        kullaniciBilgi();
    }

    private void kullaniciBilgi() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(sireac_pp);
                mevcutsifre = kullanici.getSifre();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void degistirsifre(String yeni) {
        firebaseUser.updatePassword(yeni).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userID = firebaseUser.getUid();

                reference = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(userID);
                HashMap<String, Object> map = new HashMap<>();
                map.put("sifre", yeni);

                reference.updateChildren(map);

                Toast.makeText(SifreActivity.this, "Şifreniz Değiştirildi", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SifreActivity.this, "HATA! (Şifre Değiştirilemedi!)", Toast.LENGTH_SHORT).show();

            }
        });
    }


}