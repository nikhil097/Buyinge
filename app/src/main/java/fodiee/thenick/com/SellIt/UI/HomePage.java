package fodiee.thenick.com.SellIt.UI;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import fodiee.thenick.com.SellIt.Adapter.ItemsAdapter;
import fodiee.thenick.com.SellIt.Pojo.Product;
import fodiee.thenick.com.SellIt.R;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;


    Uri fileUri;
    Bitmap userImageBitmap;

    Button bloginRegister;
    MenuItem addProduct,signOut;

    ImageView userImageView;


    ProgressDialog progressDialog;

    StorageReference storageReference;

    ProgressBar productsBar;

    Uri imageUri;

    GridView productsView;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference().child("Products");

        storageReference=FirebaseStorage.getInstance().getReference().child("images1");

        products=new ArrayList<>();

   //     getSupportActionBar().setTitle("Home");

        productsBar=findViewById(R.id.lodingProductsBar);

        progressDialog=new ProgressDialog(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addProduct=navigationView.getMenu().findItem(R.id.nav_add_product);
        signOut=navigationView.getMenu().findItem(R.id.sign_out);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home_page);
        bloginRegister=headerView.findViewById(R.id.username_tv);
        userImageView=headerView.findViewById(R.id.userImageView);

        attachDbListener();

        productsView=findViewById(R.id.productsView);
        productsView.setAdapter(new ItemsAdapter(HomePage.this,products));

        productsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HomePage.this,ProductDetail.class);
                intent.putExtra("product",products.get(position));
                startActivity(intent);
            }
        });

       // bloginRegister=navigationView.findViewById(R.id.username_tv);

        bloginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        //        auth=null;

                startActivity(new Intent(HomePage.this,SignInActivity.class));

            }
        });

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()!=null)
                {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(HomePage.this);
                    dialog.setTitle("Do you want to add/update your profile photo ?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showChoosingFile();
                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (userImageBitmap != null) {
            userImageBitmap.recycle();
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            try {
                userImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);

                uploadFile();



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
                                   imageUri = uri;
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(imageUri).build();

                                    auth.getCurrentUser().updateProfile(profileUpdates);

                                    userImageView.setBackground(null);
                                    userImageView.setImageBitmap(userImageBitmap);
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

                            Toast.makeText(HomePage.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(HomePage.this, "No File! Please select Image", Toast.LENGTH_LONG).show();

        }


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean validateInputFileName(String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(HomePage.this, "Enter file name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void showChoosingFile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);

    }


    @Override
    protected void onPause() {
        super.onPause();
      //  auth=null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            String name=auth.getCurrentUser().getDisplayName();

            if(name==null || name.equals(""))
            {
                name=auth.getCurrentUser().getPhoneNumber();
            }

            bloginRegister.setText(name);
            bloginRegister.setEnabled(false);
            addProduct.setVisible(true);
            signOut.setVisible(true);

            userImageView.setBackground(null);
            userImageView.setImageBitmap(null);

            userImageView.setBackground(getResources().getDrawable(R.drawable.baseline_supervised_user_circle_black_18dp));



                if(auth.getCurrentUser().getPhotoUrl()!=null) {

                    userImageView.setBackground(null);
                    userImageView.setImageBitmap(null);

                    Glide.with(this)
                            .load(auth.getCurrentUser().getPhotoUrl())
                            .into(userImageView);
                }
        } else {



            bloginRegister.setText("Login/Register");
            bloginRegister.setEnabled(true);
            addProduct.setVisible(false);
            signOut.setVisible(false);

            userImageView.setBackground(getResources().getDrawable(R.drawable.baseline_supervised_user_circle_black_18dp));

            userImageView.setImageBitmap(null);

            auth.signOut();
        }
        FirebaseAuth.AuthStateListener listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getCurrentUser() != null) {
                    bloginRegister.setText(auth.getCurrentUser().getEmail());
                    bloginRegister.setEnabled(false);
                    addProduct.setVisible(true);
                    signOut.setVisible(true);
                } else {
                    bloginRegister.setText("Login/Register");
                    bloginRegister.setEnabled(true);
                    addProduct.setVisible(false);
                    signOut.setVisible(false);

                }
            }
        };
    //    auth.addAuthStateListener(listener);

    }



    public void attachDbListener(){
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product=dataSnapshot.getValue(Product.class);
                product.setProductId(dataSnapshot.getKey());
                products.add(product);
                productsBar.setVisibility(View.GONE);

         //       Toast.makeText(getApplicationContext(),""+product,Toast.LENGTH_SHORT).show();
                productsView.setAdapter(new ItemsAdapter(HomePage.this,products));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

          //      GenericTypeIndicator<Map<String, String>> t = new GenericTypeIndicator<Map<String, String>>() {};
          //      Map<String, String> map = dataSnapshot.getValue(t);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public void signOutUser()
    {
        try {
           auth.signOut();

            userImageView.setBackground(getResources().getDrawable(R.drawable.baseline_supervised_user_circle_black_18dp));
            userImageView.setImageBitmap(null);
            if (auth.getCurrentUser() != null) {
                bloginRegister.setText(auth.getCurrentUser().getEmail());
                bloginRegister.setEnabled(false);
                addProduct.setVisible(true);
                signOut.setVisible(true);
            } else {
                bloginRegister.setText("Login/Register");
                bloginRegister.setEnabled(true);
                addProduct.setVisible(false);
                signOut.setVisible(false);

            }
           //     auth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Signout Successful", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_product) {
            if(auth!=null)
            {
                startActivity(new Intent(HomePage.this,MainActivity.class));
            }
        }
        else if(id==R.id.sign_out){
                signOutUser();
            }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
