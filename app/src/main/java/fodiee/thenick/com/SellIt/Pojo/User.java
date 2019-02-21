package fodiee.thenick.com.SellIt.Pojo;

public class User
{
    String userName;
    String userId;
    String userImage;


    public User(String userName,String userImage)
    {
        this.userName=userName;
        this.userImage=userImage;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
