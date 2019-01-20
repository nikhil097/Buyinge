package fodiee.thenick.com.zerseydemo.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Map;

import fodiee.thenick.com.zerseydemo.Adapter.ItemsAdapter;
import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;


    Button bloginRegister;
    MenuItem addProduct,signOut;

    ProgressBar productsBar;

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

        products=new ArrayList<>();

        getSupportActionBar().setTitle("Home");

        productsBar=findViewById(R.id.lodingProductsBar);

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

                startActivity(new Intent(HomePage.this,SignInActivity.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (auth != null) {
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
    }

    public void attachDbListener(){
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product=dataSnapshot.getValue(Product.class);
                product.setProductId(dataSnapshot.getKey());
                products.add(product);
                productsBar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),""+product,Toast.LENGTH_SHORT).show();
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
           onResume();
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
