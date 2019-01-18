package fodiee.thenick.com.zerseydemo.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class AddProduct extends AppCompatActivity {

    AppCompatSpinner category_spinner;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        category_spinner=findViewById(R.id.category_spinner);

        String[] categoryType=getResources().getStringArray(R.array.category_array);
        ArrayAdapter adapter=new ArrayAdapter(AddProduct.this,R.layout.category_spinner_item,categoryType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);



        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

        myRef.push().setValue(new Product("Tv","Sony","32inch","50000"));




    }
}
