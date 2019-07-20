package com.andromeda.ara;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class Adapter
        extends RecyclerView.Adapter<Adapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels; //= MainActivity.RssClass.getFeedList();

    //private final OnItemClickListener listener;

    public Adapter(List<RssFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;

    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);

        return holder;
    }
    public interface OnItemClickListener {
        void onItemClick(RssFeedModel item);
    }


    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView) holder.rssFeedView.findViewById(R.id.item_number)).setText(rssFeedModel.title);
        ((TextView) holder.rssFeedView.findViewById(R.id.content)).setText(rssFeedModel.description);
        ((TextView) holder.rssFeedView.findViewById(R.id.url2)).setText(rssFeedModel.link);
        if (rssFeedModel.image != "") {
            InputStream is = null;
            try {
                is = (InputStream) new URL(rssFeedModel.image).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable draw = Drawable.createFromStream(is, "src name");
             holder.rssFeedView.findViewById(R.id.item_image_view).setBackground(draw);
        }
        //((TextView)holder.rssFeedView.findViewById(R.id.item_number)).setText(R.string.test);
        //((TextView)holder.rssFeedView.findViewById(R.id.content)).setText(R.string.test);
        //((TextView)holder.rssFeedView.findViewById(R.id.url2)).setText(R.string.test);
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
        //return 1;
    }

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }
}

