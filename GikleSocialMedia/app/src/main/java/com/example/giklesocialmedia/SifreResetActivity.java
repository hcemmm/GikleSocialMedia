package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SifreResetActivity  extends AppCompatActivity {

    ImageView sifresifir_geri;
    EditText sifresifir_mail;
    Button sifresifir_buton;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_reset);

        sifresifir_mail = findViewById(R.id.sifresifirla_mailtext);
        sifresifir_buton = findViewById(R.id.sifresifirla_buton);
        sifresifir_geri = findViewById(R.id.sifresifirla_kapat);
        firebaseAuth = FirebaseAuth.getInstance();

        sifresifir_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sifresifir_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sifresifir_mail.getText().toString();

                if (email.equals("")){
                    Toast.makeText(SifreResetActivity.this, "E-Posta adresinizi yazın...", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SifreResetActivity.this, "E-posta adresinizi kontrol ediniz.", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SifreResetActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}