package fodiee.thenick.com.zerseydemo.UI;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fodiee.thenick.com.zerseydemo.R;

public class MainActivity extends AppCompatActivity {



    FloatingActionButton addProductFAB;


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




    }
}
