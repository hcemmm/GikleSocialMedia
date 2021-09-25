package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Model.Kullanici;
import com.example.giklesocialmedia.Model.KullaniciDetay;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class ProfilDuzenActivity extends  AppCompatActivity {

    ImageView edit_kapat, edit_pp;
    TextView edit_kaydet, edit_textfotodegis;
    MaterialEditText edit_adsoyad, edit_kullaniciadi, edit_bio;
    MaterialEditText edit_isyeri,edit_ispozisyon,edit_websitesi,edit_facebook,edit_twitter,edit_instagram,edit_telefon,edit_mail,edit_yas,edit_universite,edit_universitebolum,edit_hakkimda;

    FirebaseUser firebaseUser;

    private Uri mResimUri;
    private StorageTask uploadTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_duzen);

        edit_kapat = findViewById(R.id.profedit_kapat);
        edit_pp = findViewById(R.id.profedit_pp);
        edit_kaydet = findViewById(R.id.profedit_kaydet);
        edit_textfotodegis = findViewById(R.id.profedit_degistirfototext);
        edit_adsoyad = findViewById(R.id.profedit_adsoyad);
        edit_kullaniciadi = findViewById(R.id.profedit_kullaniciadi);
        edit_bio = findViewById(R.id.profedit_bio);

        edit_isyeri = findViewById(R.id.profedit_isyeri);
        edit_ispozisyon = findViewById(R.id.profedit_ispozisyon);
        edit_facebook = findViewById(R.id.profedit_facebook);
        edit_twitter = findViewById(R.id.profedit_twitter);
        edit_instagram = findViewById(R.id.profedit_instagram);
        edit_telefon = findViewById(R.id.profedit_telefon);
        edit_mail = findViewById(R.id.profedit_mail);
        edit_yas = findViewById(R.id.profedit_yas);
        edit_universite = findViewById(R.id.profedit_universite);
        edit_universitebolum = findViewById(R.id.profedit_universitebolum);
        edit_hakkimda = findViewById(R.id.profedit_hakkimda);
        edit_websitesi = findViewById(R.id.profedit_website);




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("Yuklenenler");

        DatabaseReference referencedetay = FirebaseDatabase.getInstance().getReference("KullaniciDetay").child(firebaseUser.getUid());
        referencedetay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KullaniciDetay kullaniciDetay = dataSnapshot.getValue(KullaniciDetay.class);

                edit_isyeri.setText(kullaniciDetay.getIsyeri());
                edit_ispozisyon.setText(kullaniciDetay.getIspozisyon());
                edit_facebook.setText(kullaniciDetay.getFacebookadres());
                edit_twitter.setText(kullaniciDetay.getTwitteradres());
                edit_instagram.setText(kullaniciDetay.getInstagramadress());
                edit_telefon.setText(kullaniciDetay.getTelefon());
                edit_mail.setText(kullaniciDetay.getMailadres());
                edit_yas.setText(kullaniciDetay.getYas());
                edit_universite.setText(kullaniciDetay.getUniversiteadi());
                edit_universitebolum.setText(kullaniciDetay.getUniversitebolum());
                edit_hakkimda.setText(kullaniciDetay.getHakkimda());
                edit_websitesi.setText(kullaniciDetay.getWebsitesi());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                edit_adsoyad.setText(kullanici.getAdvesoyad());
                edit_kullaniciadi.setText(kullanici.getKullaniciadi());
                edit_bio.setText(kullanici.getBio());
                Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(edit_pp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(edit_adsoyad.getText().toString(),
                        edit_kullaniciadi.getText().toString(),
                        edit_bio.getText().toString(),
                edit_isyeri.getText().toString(),
                        edit_ispozisyon.getText().toString(),
                        edit_facebook.getText().toString(),
                        edit_twitter.getText().toString(),
                        edit_instagram.getText().toString(),
                        edit_telefon.getText().toString(),
                        edit_mail.getText().toString(),
                        edit_yas.getText().toString(),
                        edit_universite.getText().toString(),
                        edit_universitebolum.getText().toString(),
                        edit_websitesi.getText().toString(),
                        edit_hakkimda.getText().toString());


            }
        });

        edit_textfotodegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfilDuzenActivity.this);
            }
        });

        edit_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfilDuzenActivity.this);
            }
        });
    }

    private void updateProfile(String adsoyad, String kullaniciadi, String bio, String isyeri, String ispozisyon, String facebookadres, String twitteradres,
                               String instagramadress, String telefon, String mailadres, String yas, String universiteadi
            , String universitebolum,String websitesi, String hakkimda){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("advesoyad", adsoyad);
        map.put("kullaniciadi", kullaniciadi);
        map.put("bio", bio);
        reference.updateChildren(map);

        DatabaseReference referencedetay = FirebaseDatabase.getInstance().getReference().child("KullaniciDetay").child(firebaseUser.getUid());

        HashMap<String, Object> mapdetay = new HashMap<>();
        mapdetay.put("isyeri", isyeri);
        mapdetay.put("ispozisyon", ispozisyon);
        mapdetay.put("facebookadres", facebookadres);
        mapdetay.put("twitteradres", twitteradres);
        mapdetay.put("instagramadress", instagramadress);
        mapdetay.put("telefon", telefon);
        mapdetay.put("mailadres", mailadres);
        mapdetay.put("yas", yas);
        mapdetay.put("universiteadi", universiteadi);
        mapdetay.put("universitebolum", universitebolum);
        mapdetay.put("hakkimda", hakkimda);
        mapdetay.put("websitesi", websitesi);
        mapdetay.put("kullanicid", FirebaseAuth.getInstance().getCurrentUser().getUid());

        referencedetay.updateChildren(mapdetay);


        Toast.makeText(ProfilDuzenActivity.this, "Profiliniz güncellendi!", Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void yukleResim(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Güncelleniyor...");
        pd.show();
        if (mResimUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mResimUri));

            uploadTask = fileReference.putFile(mResimUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("profil_pp", ""+miUrlOk);
                        reference.updateChildren(map1);

                        pd.dismiss();

                    } else {
                        Toast.makeText(ProfilDuzenActivity.this, "HATA(onComplete)", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfilDuzenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(ProfilDuzenActivity.this, "Fotoğraf seçilmedi!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mResimUri = result.getUri();

            yukleResim();

        } else {
            Toast.makeText(this, "Bir sorunla karşılaşıldı!(onActivityResult)", Toast.LENGTH_SHORT).show();
        }
    }
}