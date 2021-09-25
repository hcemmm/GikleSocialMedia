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

public class PostActivity extends AppCompatActivity {

    private Uri mResimUri;
    String miUrlOk = "";
    private StorageTask uploadTask;
    StorageReference storageRef;
    FirebaseUser firebaseUser;

    ImageView postactivity_pp;
    ImageView postactivity_kapat, postactivity_fotografekle;
    TextView postactivity_post,postactivity_kullaniciadi;;
    EditText postactivity_aciklama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postactivity_kapat = findViewById(R.id.postactivity_kapat);
        postactivity_fotografekle = findViewById(R.id.postactivity_fotografekle);
        postactivity_post = findViewById(R.id.postactivity_post);
        postactivity_aciklama = findViewById(R.id.postactivity_aciklama);
        postactivity_pp = findViewById(R.id.postactivity_pp);
        postactivity_kullaniciadi = findViewById(R.id.postactivity_kullaniciadi);

        storageRef = FirebaseStorage.getInstance().getReference("Postlar");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                postactivity_kullaniciadi.setText(kullanici.getKullaniciadi());
                Glide.with(getApplicationContext()).load(kullanici.getProfil_pp()).into(postactivity_pp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postactivity_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        postactivity_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yukleResim();
            }
        });


        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void yukleResim(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Gönderi Paylaşılıyor...");
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
                        miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postlar");

                        String postid = reference.push().getKey();

                        Calendar callfordate = Calendar.getInstance();
                        SimpleDateFormat currentdate = new
                                SimpleDateFormat("dd.MM.yyyy");
                        final  String savedate = currentdate.format(callfordate.getTime());


                        String time = savedate;

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postiresim", miUrlOk);
                        hashMap.put("postaciklama", postactivity_aciklama.getText().toString());
                        hashMap.put("postpaylasan", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("postzaman", time);

                        reference.child(postid).setValue(hashMap);

                        pd.dismiss();

                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "HATA(yukleResim)", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(PostActivity.this, "Fotoğraf seçilmedi!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mResimUri = result.getUri();

            postactivity_fotografekle.setImageURI(mResimUri);
        } else {
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }
}