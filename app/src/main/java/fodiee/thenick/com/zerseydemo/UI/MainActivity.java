package fodiee.thenick.com.zerseydemo.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fodiee.thenick.com.zerseydemo.Adapter.ItemsAdapter;
import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class MainActivity extends AppCompatActivity {



    FirebaseAuth auth;

    FloatingActionButton addProductFAB;

    ArrayList<Product> myProducts;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myref;
    GridView myProductsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addProductFAB=findViewById(R.id.addProductFloatingButton);

        addProductFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddProduct.class));
            }
        });

        auth=FirebaseAuth.getInstance();

        myProducts=new ArrayList<>();

        firebaseDatabase=FirebaseDatabase.getInstance();
        myref=firebaseDatabase.getReference().child("Products");

        myProductsView=findViewById(R.id.my_productsView);
        myProductsView.setAdapter(new ItemsAdapter(MainActivity.this,myProducts));

        attachDbListener();
    }

    public void attachDbListener(){
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product=dataSnapshot.getValue(Product.class);
                if(product.getUploadedBy().equals(auth.getCurrentUser().getEmail())) {
                    myProducts.add(product);
                    Toast.makeText(getApplicationContext(), "" + product, Toast.LENGTH_SHORT).show();
                    myProductsView.setAdapter(new ItemsAdapter(MainActivity.this, myProducts));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

}
