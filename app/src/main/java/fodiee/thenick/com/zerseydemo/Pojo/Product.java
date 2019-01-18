package fodiee.thenick.com.zerseydemo.Pojo;

import android.graphics.Bitmap;
import android.media.Image;

public class Product {

    String category;
    String description;
    String title;
    Bitmap image;
    String comments;
    String uploadedBy;
    String expectedPrice;
    Image bitmap;

    public Product(String category,String title,String description,String expectedPrice,String uploadedBy,Image bitmap)
    {
        this.category=category;
        this.title=title;
        this.description=description;
        this.expectedPrice=expectedPrice;
        this.uploadedBy=uploadedBy;
        this.bitmap=bitmap;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

