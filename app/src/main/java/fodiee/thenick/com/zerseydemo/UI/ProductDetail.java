package fodiee.thenick.com.zerseydemo.UI;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class ProductDetail extends AppCompatActivity {

    TextView title_tv,category_tv,description_tv,expectedPrice_tv;
    ImageView productView;
    Button bLike,bComment,bBid,bPostComment;
    EditText comment_et;
    Product product;


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

        bPostComment=findViewById(R.id.postCooment);
        comment_et=findViewById(R.id.comments_et);

        bLike=findViewById(R.id.likeButton);
        bComment=findViewById(R.id.commentButton);
        bBid=findViewById(R.id.bidButton);


        bComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bComment.setVisibility(View.GONE);
                bLike.setVisibility(View.GONE);
                bBid.setVisibility(View.GONE);

                bPostComment.setVisibility(View.VISIBLE);
                comment_et.setVisibility(View.VISIBLE);

            }
        });




        title_tv.setText(title_tv.getText().toString()+product.getTitle());
        category_tv.setText(category_tv.getText().toString()+product.getCategory());
        expectedPrice_tv.setText(expectedPrice_tv.getText().toString()+product.getExpectedPrice());
        productView.setImageBitmap(base64ToImage(product.getBitmap()));
        description_tv.setText(description_tv.getText().toString()+product.getDescription());

    }


    public Bitmap base64ToImage(String encodedImage)
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
