package fodiee.thenick.com.zerseydemo.UI;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    String title,description,imageString;

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
                if(TextUtils.isEmpty(title_et.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter title",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(description_et.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please enter some description",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(imageString))
                {
                    Toast.makeText(getApplicationContext(),"Please upload an image",Toast.LENGTH_SHORT).show();
                }
                else{
                    myRef.push().setValue(new Product((String)category_spinner.getSelectedItem(),title_et.getText().toString().trim(),description_et.getText().toString().trim(),expectedPrice.getText().toString().trim(),auth.getCurrentUser().getEmail(),imageString)).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
