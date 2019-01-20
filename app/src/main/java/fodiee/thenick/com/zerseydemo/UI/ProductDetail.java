package fodiee.thenick.com.zerseydemo.UI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class ProductDetail extends AppCompatActivity {

    TextView title_tv,category_tv,description_tv,expectedPrice_tv;
    ImageView productView;
    Button bLike,bComment, bReply,bPostComment;
    EditText comment_et;
    Product product;
    ArrayList<String> comments;
    ArrayList<String> likes;
    FirebaseDatabase firebase;
    DatabaseReference myRef;
    FirebaseAuth auth;

    boolean isLiked;
    String path;

    ListView commentsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product= (Product) getIntent().getSerializableExtra("product");

        title_tv=findViewById(R.id.product_title_tv);
        category_tv=findViewById(R.id.product_category_tv);
        expectedPrice_tv=findViewById(R.id.product_expectedPrice_tv);
        productView=findViewById(R.id.product_fullView);
        description_tv=findViewById(R.id.product_desciption_tv);

        commentsView=findViewById(R.id.commentsLv);

        comments=new ArrayList<>();
        likes=new ArrayList<>();

        commentsView.setAdapter(new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,comments));

        firebase=FirebaseDatabase.getInstance();
        myRef=firebase.getReference().child("Products");

        auth=FirebaseAuth.getInstance();

        bPostComment=findViewById(R.id.postCooment);
        comment_et=findViewById(R.id.comments_et);

        bLike=findViewById(R.id.likeButton);
        bComment=findViewById(R.id.commentButton);
        bReply =findViewById(R.id.bidButton);


        bComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bComment.setVisibility(View.GONE);
                bLike.setVisibility(View.GONE);
                bReply.setVisibility(View.GONE);

                bPostComment.setVisibility(View.VISIBLE);
                comment_et.setVisibility(View.VISIBLE);

            }
        });

        bPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child(product.getProductId()).child("comments").push().setValue(auth.getCurrentUser().getEmail()+": "+comment_et.getText().toString().trim());

                comment_et.setText("");
                comment_et.setVisibility(View.GONE);
                bPostComment.setVisibility(View.GONE);
                bComment.setVisibility(View.VISIBLE);
                bLike.setVisibility(View.VISIBLE);
                bReply.setVisibility(View.VISIBLE);

            }
        });

        bLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLiked) {
                    myRef.child(product.getProductId()).child("likes").push().setValue(auth.getCurrentUser().getEmail());
                    bLike.setText("Unlike");



                }
                else{

                    Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
                    myRef.child(product.getProductId()).child("likes").child(path).setValue(null);
                    path=null;
                    isLiked=false;
                    bLike.setText("Like");
                }
            }
        });

        attachDbListener();



        title_tv.setText(title_tv.getText().toString()+product.getTitle());
        category_tv.setText(category_tv.getText().toString()+product.getCategory());
        expectedPrice_tv.setText(expectedPrice_tv.getText().toString()+product.getExpectedPrice());
        productView.setImageBitmap(base64ToImage(product.getBitmap()));
        description_tv.setText(description_tv.getText().toString()+product.getDescription());

    }

    @Override
    public void onBackPressed() {
        if(bPostComment.getVisibility()==View.VISIBLE)
        {
            comment_et.setVisibility(View.GONE);
            bPostComment.setVisibility(View.GONE);
            bComment.setVisibility(View.VISIBLE);
            bLike.setVisibility(View.VISIBLE);
            bReply.setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
        }
    }



    public void attachDbListener()
    {

        myRef.child(product.getProductId()).child("comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String comment=dataSnapshot.getValue(String.class);

                comments.add(comment);
                commentsView.setAdapter(new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,comments));
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


        myRef.child(product.getProductId()).child("likes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String likedBy=dataSnapshot.getValue(String.class);
                if(likedBy.equals(product.getUploadedBy()))
                {
                    isLiked=true;
                    bLike.setText("Unlike");
                    path=dataSnapshot.getKey();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String likedBy=dataSnapshot.getValue(String.class);
                if(likedBy.equals(product.getUploadedBy()))
                {
                    isLiked=true;
                    bLike.setText("Liked");
                    path=dataSnapshot.getKey();
                }

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


    public Bitmap base64ToImage(String encodedImage)
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
