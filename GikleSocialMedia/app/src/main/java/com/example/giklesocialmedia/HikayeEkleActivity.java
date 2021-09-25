package com.example.giklesocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class HikayeEkleActivity extends AppCompatActivity {

    private Uri mResimUri;
    String miUrlOk = "";
    private StorageTask uploadTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hikaye_ekle);

        storageRef = FirebaseStorage.getInstance().getReference("hikaye");

        CropImage.activity()
                .setAspectRatio(9,16)
                .start(HikayeEkleActivity.this);

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void yukleHikaye(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Gönderiliyor...");
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

                        String myid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hikaye")
                                .child(myid);

                        String storyid = reference.push().getKey();
                        long timeend = System.currentTimeMillis()+86400000; // 1 gun

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("hikayefotourl", miUrlOk);
                        hashMap.put("hikayetimestart", ServerValue.TIMESTAMP);
                        hashMap.put("hikayetimeend", timeend);
                        hashMap.put("hikayeid", storyid);
                        hashMap.put("hikayekullaniciid", myid);

                        reference.child(storyid).setValue(hashMap);

                        pd.dismiss();

                        finish();

                    } else {
                        Toast.makeText(HikayeEkleActivity.this, "HATA(onComplete)", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HikayeEkleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(HikayeEkleActivity.this, "Resim seçilmedi!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mResimUri = result.getUri();

            yukleHikaye();

        } else {
            Toast.makeText(this, "Bir sorun var! (onActivityResult)", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HikayeEkleActivity.this, MainActivity.class));
            finish();
        }
    }
}