package fodiee.thenick.com.zerseydemo.UI;

import android.animation.TimeAnimator;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class AddProduct extends AppCompatActivity {

    AppCompatSpinner category_spinner;
    Button selectPhoto;

    Bitmap bitmap;
    Uri fileUri;

    StorageReference storageReference;

    ProgressBar addProductBar;

    ProgressDialog progressDialog;

    String title,description,imageString,imgUrl;

    EditText title_et,description_et,expectedPrice;

    Button bAddProduct;

    FirebaseDatabase database;
    FirebaseAuth auth;
    int CHOOSING_IMAGE_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        category_spinner=findViewById(R.id.category_spinner);
        selectPhoto=findViewById(R.id.uploadPhoto);
        bAddProduct=findViewById(R.id.addProduct_btn);
        expectedPrice=findViewById(R.id.expectedPrice_et);

        auth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);

        storageReference=FirebaseStorage.getInstance().getReference().child("images");

        addProductBar=findViewById(R.id.addProductProgress);

        title_et=findViewById(R.id.title_et);
        description_et=findViewById(R.id.description_et);

        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Products");

        String[] categoryType=getResources().getStringArray(R.array.category_array);
        ArrayAdapter adapter=new ArrayAdapter(AddProduct.this,R.layout.category_spinner_item,categoryType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bitmap==null)
                {
                    showChoosingFile();
                }
                else{

                }

            }
        });

        bAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(title_et.getText().toString().trim()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter title",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(description_et.getText().toString().trim()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter some description",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(imgUrl))
                {
                    Toast.makeText(getApplicationContext(),"Please upload an image",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(expectedPrice.getText().toString().trim()))
                {
                    Toast.makeText(AddProduct.this,"Please enter expected price",Toast.LENGTH_SHORT).show();
                }
                else{
                    addProductBar.setVisibility(View.VISIBLE);
                    String uploadedBy=auth.getCurrentUser().getEmail();
                    if(uploadedBy==null)
                    {
                        uploadedBy=auth.getCurrentUser().getPhoneNumber();
                    }
                    else if(uploadedBy.equals(""))
                    {
                        uploadedBy=auth.getCurrentUser().getPhoneNumber();
                    }
                    myRef.push().setValue(new Product((String)category_spinner.getSelectedItem(),title_et.getText().toString().trim(),description_et.getText().toString().trim(),expectedPrice.getText().toString().trim(),uploadedBy,imgUrl)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Product Added Successfully",Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"There is some error",Toast.LENGTH_SHORT).show();
                            }
                            addProductBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });









    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (bitmap != null) {
            bitmap.recycle();
        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                imageString=imageToBase64(bitmap);
                uploadFile();
                selectPhoto.setText("Photo Uploaded");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void uploadFile() {
        if (fileUri != null) {

            String fileName = fileUri.getLastPathSegment();
            if (!validateInputFileName(fileName)) {
                return;
            }

            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            progressDialog.setCancelable(false);

            final StorageReference fileRef = storageReference.child(fileName + "." + getFileExtension(fileUri));
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

//                            Log.e(TAG, "Uri: " + taskSnapshot.getDownloadUrl());
                            Log.e("tag1", "Name: " + taskSnapshot.getMetadata().getName());


                            fileUri = null;
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgUrl = uri.toString();
                                    //                 Log.d("IURI", imgUrl1);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("IURI", e.toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Toast.makeText(AddProduct.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            // percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    })
                    .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused!");
                        }
                    });
        } else {
            Toast.makeText(AddProduct.this, "No File! Please select Image", Toast.LENGTH_LONG).show();

        }


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean validateInputFileName(String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(AddProduct.this, "Enter file name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public String imageToBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }



    public void showChoosingFile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);

    }

}
