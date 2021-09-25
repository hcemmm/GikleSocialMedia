package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText register_kullaniciadi, register_adsoyad, register_email, register_sifre;
    Button register_kayitol;
    TextView register_girisyap;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_kullaniciadi = findViewById(R.id.register_kullaniciadi);
        register_email = findViewById(R.id.register_email);
        register_adsoyad = findViewById(R.id.register_adsoyad);
        register_sifre = findViewById(R.id.register_sifre);
        register_kayitol = findViewById(R.id.register_kayitolbuton);
        register_girisyap = findViewById(R.id.register_girisyapin);

        auth = FirebaseAuth.getInstance();

        register_girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register_kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Lütfen Bekleyin...");
                pd.show();

                String str_kullaniciadi = register_kullaniciadi.getText().toString();
                String str_adsoyad = register_adsoyad.getText().toString();
                String str_email = register_email.getText().toString();
                String str_sifre = register_sifre.getText().toString();

                if (TextUtils.isEmpty(str_kullaniciadi) || TextUtils.isEmpty(str_adsoyad) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_sifre)){
                    Toast.makeText(RegisterActivity.this, "Tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                } else if(str_sifre.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Parolanız 6 karakterden uzun olmalıdır!", Toast.LENGTH_SHORT).show();
                } else {
                    kayitol(str_kullaniciadi, str_adsoyad, str_email, str_sifre);
                }
            }
        });
    }

    public void kayitol(final String kullaniciadi, final String adsoyad, String email, String sifre){
        auth.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(userID);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("kullaniciadi", kullaniciadi.toLowerCase());
                            map.put("advesoyad", adsoyad);
                            map.put("profil_pp", "");
                            map.put("bio", "");
                            map.put("durum", "offline");
                            map.put("mail", email);
                            map.put("sifre", sifre);

                            DatabaseReference referencedetay = FirebaseDatabase.getInstance().getReference().child("KullaniciDetay").child(firebaseUser.getUid());

                            HashMap<String, Object> mapdetay = new HashMap<>();
                            mapdetay.put("isyeri", "");
                            mapdetay.put("ispozisyon", "");
                            mapdetay.put("facebookadres", "");
                            mapdetay.put("twitteradres", "");
                            mapdetay.put("instagramadress", "");
                            mapdetay.put("telefon", "");
                            mapdetay.put("mailadres", "");
                            mapdetay.put("yas", "");
                            mapdetay.put("universiteadi", "");
                            mapdetay.put("universitebolum", "");
                            mapdetay.put("hakkimda", "");
                            mapdetay.put("kullaniciadi", kullaniciadi.toLowerCase());

                            referencedetay.updateChildren(mapdetay);

                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Bu e-posta ile kayıt olamazsınız.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}