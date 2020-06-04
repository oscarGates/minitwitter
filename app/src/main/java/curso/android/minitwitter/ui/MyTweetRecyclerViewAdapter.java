package curso.android.minitwitter.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import curso.android.minitwitter.R;
import curso.android.minitwitter.common.Constantes;
import curso.android.minitwitter.common.SharedPreferencesManager;
import curso.android.minitwitter.retrofit.response.Like;
import curso.android.minitwitter.retrofit.response.Tweet;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context ctx;
    String username;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items ){
        mValues = items;
        ctx = context;
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null) {
            holder.mItem = mValues.get(position);
            holder.tvUsername.setText(holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            if (!holder.mItem.getUser().getPhotoUrl().equals("")) {
                Glide.with(ctx)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + holder.mItem.getUser().getPhotoUrl())
                        .into(holder.ivAvatar);
            }

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
            holder.tvLikesCount.setTypeface(null, Typeface.BOLD);

            for (Like like : holder.mItem.getLikes()) {
                if (like.getUsername().equals(username)) {
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.ivLike);
                    holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                    holder.tvLikesCount.setTypeface(null, Typeface.NORMAL);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mValues == null)
            return 0;
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike =  view.findViewById(R.id.imageViewLike);
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvMessage = view.findViewById(R.id.textViewMessage);
            tvLikesCount = view.findViewById(R.id.textViewLikes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUsername.getText() + "'";
        }
    }

    public void setData(List<Tweet> tweetList){
        this.mValues = tweetList;
        notifyDataSetChanged();
    }
}
