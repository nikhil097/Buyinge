package fodiee.thenick.com.SellIt.Pojo;

public class Message
{

     String sentBy;
     String message;
     String sentTo;
     String productId;


     public Message()
     {

     }

     public Message(String sentBy,String message,String sentTo,String productId)
     {

         this.message=message;
         this.sentBy=sentBy;
         this.sentTo=sentTo;
         this.productId=productId;

     }

    public String getSentTo() {
        return sentTo;
    }

    public String getProductId() {

        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getSentBy(){
        return sentBy;
    }

    public void setSentBy(String uploadedBy) {
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
