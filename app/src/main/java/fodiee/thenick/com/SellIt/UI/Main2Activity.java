package fodiee.thenick.com.SellIt.UI;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fodiee.thenick.com.SellIt.Pojo.Message;
import fodiee.thenick.com.SellIt.Pojo.Product;
import fodiee.thenick.com.SellIt.R;

public class Main2Activity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    ArrayList<Message> myMesages;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setTitle("Replies");

        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        myRef=firebaseDatabase.getReference().child("Products");

        myMesages=new ArrayList<>();

      //  myRef.child("replies").push().setValue(new Message("hi","hello"));

        myRef.child("-LWdy65tc5ZzxRlnXKvw").child("replies").push().setValue(new Message("-LWdy65tc5ZzxRlnXKvw","Hi","helllo","Nevermind"));


//        attachDbListener();

    }


    public void sendMesage(String productId,String sentBy,String sentTo,String message)
    {

        myRef.child(productId).child("replies").push().setValue(new Message(productId,sentBy,sentTo,message));
    }



    public void attachDbListener() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Product product=dataSnapshot.getValue(Product.class);

                if(product.getReplies()!=null) {
                    for (int i = 0; i < product.getReplies().size(); i++) {
                        if (product.getReplies().get(i).getSentTo() == auth.getCurrentUser().getEmail()) {
                            product.getReplies().get(i).setProductId(dataSnapshot.getKey());
                            myMesages.add(product.getReplies().get(i));
                            product.setProductId(dataSnapshot.getKey());
                            Toast.makeText(getApplicationContext(), "" + product, Toast.LENGTH_SHORT).show();
                            //       myProductsView.setAdapter(new ItemsAdapter(Main2Activity.this, myProducts));
                        }
                    }
                }
          //      myProductsBar.setVisibility(View.GONE);
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


}
