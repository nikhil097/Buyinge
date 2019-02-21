package fodiee.thenick.com.SellIt.UI;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fodiee.thenick.com.SellIt.Pojo.Product;
import fodiee.thenick.com.SellIt.R;
import id.zelory.compressor.Compressor;

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
    private String path2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        category_spinner=findViewById(R.id.category_spinner);
        selectPhoto=findViewById(R.id.uploadPhoto);
        bAddProduct=findViewById(R.id.addProduct_btn);
        expectedPrice=findViewById(R.id.expectedPrice_et);

        auth=FirebaseAuth.getInstance();

        isStoragePermissionGranted();

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

        if(requestCode==CHOOSING_IMAGE_REQUEST) {

            if (bitmap != null) {
                bitmap.recycle();
            }


            Toast.makeText(this, "" + data.getData(), Toast.LENGTH_SHORT).show();

            path2 = getRealPathFromURI(data.getData());

            Log.v("path2", path2 + "");

            if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                fileUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                    imageString = imageToBase64(bitmap);
                    compressImage(bitmap);
                    uploadFile();
                    selectPhoto.setText("Photo Uploaded");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }




    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("tag","Permission is granted");
                return true;
            } else {

                Log.v("tag","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("tag","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("tag","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }



    public void compressImage(Bitmap actualBitmap) throws IOException {


        Bitmap compressedImage = new Compressor(this)
                .setMaxWidth(600)
                .setMaxHeight(800)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToBitmap(new File(path2));



        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        compressedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        progressDialog.show();

        UploadTask uploadTask = storageReference.child("image: "+fileUri.getLastPathSegment()).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Toast.makeText(AddProduct.this, "Compresed image uploaded to Firebase", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();


          //      database.getReference().child("imageUrls").push().setValue();
            }
        });

    }

    public void uploadImage(Bitmap cpmpressedImage)
    {

    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
        public static String getDataColumn(Context context, Uri uri, String selection,
                String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());

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
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);

    }

}
