package fodiee.thenick.com.SellIt.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fodiee.thenick.com.SellIt.Pojo.Comment;
import fodiee.thenick.com.SellIt.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {


    ArrayList<Comment> commentsList;

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder commentsHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public CommentsAdapter(ArrayList<Comment> commentsList)
    {

    }


    public class CommentsHolder extends RecyclerView.ViewHolder {
        public TextView userName, commentBody;
        public ImageView userImage;

        public CommentsHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.commentUserNameTv);
            commentBody = (TextView) view.findViewById(R.id.comments_tv);
            userImage=view.findViewById(R.id.commentUserNameTv);

        }
    }





}
