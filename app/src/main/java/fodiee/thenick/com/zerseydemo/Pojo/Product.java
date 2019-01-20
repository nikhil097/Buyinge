package fodiee.thenick.com.zerseydemo.Pojo;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ScrollView;
import android.widget.SectionIndexer;

import com.google.firebase.database.GenericTypeIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Product implements Serializable {

    String category;
    String description;
    String title;
    Bitmap image;
    String uploadedBy;
    String expectedPrice;
    String bitmap;

    String productId;
    String commentsId;



    HashMap<String,String> noOfLikes;
    HashMap<String,String> comments;

    HashMap<String,Message> replies;

    Product(){

    }


    public Product(String category,String title,String description,String expectedPrice,String uploadedBy,String bitmap)
    {
        this.category=category;
        this.title=title;
        this.description=description;
        this.expectedPrice=expectedPrice;
        this.uploadedBy=uploadedBy;
        this.bitmap=bitmap;
    }


    public HashMap<String, Message> getReplies(){

        return replies;
    }

    public void setReplies(HashMap<String, Message> replies) {
        this.replies = replies;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(String expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return bitmap;
    }

    public void setImage(String image) {
        this.bitmap = bitmap;
    }


}

