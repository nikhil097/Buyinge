package fodiee.thenick.com.zerseydemo.Pojo;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ScrollView;
import android.widget.SectionIndexer;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

    String category;
    String description;
    String title;
    Bitmap image;
    String uploadedBy;
    String expectedPrice;
    String bitmap;

    ArrayList<String> noOfLikes;
    ArrayList<String> comments;

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

