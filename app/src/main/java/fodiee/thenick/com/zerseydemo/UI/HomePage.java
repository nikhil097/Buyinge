package fodiee.thenick.com.zerseydemo.UI;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import fodiee.thenick.com.zerseydemo.R;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;

    Button bloginRegister;
    MenuItem addProduct,signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth=FirebaseAuth.getInstance();

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
        if (auth!=null)
        {
            if(auth.getCurrentUser()!=null)
            {
                Toast.makeText(getApplicationContext(),auth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
                bloginRegister.setText(auth.getCurrentUser().getEmail());
                bloginRegister.setEnabled(false);
                addProduct.setVisible(true);
                signOut.setVisible(true);
            }
            else
            {
                bloginRegister.setText("Login/Register");
                bloginRegister.setEnabled(true);
                addProduct.setVisible(false);
                signOut.setVisible(false);

            }
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
                startActivity(new Intent(HomePage.this,AddProduct.class));
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
