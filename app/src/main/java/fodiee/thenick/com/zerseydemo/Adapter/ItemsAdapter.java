package fodiee.thenick.com.zerseydemo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.BasicPermission;
import java.util.ArrayList;

import fodiee.thenick.com.zerseydemo.Pojo.Product;
import fodiee.thenick.com.zerseydemo.R;

public class ItemsAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Product> products;

    public ItemsAdapter(Context c,ArrayList<Product> products) {
        mContext = c;
        this.products=products;
    }

    public int getCount() {
        return products.size();
    }

    public Object getItem(int position) {
        return products.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imagePreview;
        TextView titlePreview;
        View view;
        if (convertView == null) {
            convertView=LayoutInflater.from(mContext).inflate(R.layout.gridview_product_item,parent,false);
        }

        imagePreview=convertView.findViewById(R.id.product_preview);
        titlePreview=convertView.findViewById(R.id.title_preview);
        titlePreview.setText(products.get(position).getTitle());
        imagePreview.setImageBitmap(base64ToImage(products.get(position).getBitmap()));
        return convertView;
    }

    public Bitmap base64ToImage(String encodedImage)
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
