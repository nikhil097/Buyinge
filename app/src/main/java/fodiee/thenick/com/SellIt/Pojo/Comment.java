package fodiee.thenick.com.SellIt.Pojo;

import android.graphics.Bitmap;

public class Comment {

    String userName;
    String commentBody;

    Bitmap userImage;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }
}
