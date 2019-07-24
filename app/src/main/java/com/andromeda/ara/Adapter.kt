package com.andromeda.ara

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.Adapter.FeedModelViewHolder

import java.io.IOException
import java.io.InputStream
import java.net.URL


class Adapter
//private final OnItemClickListener listener;

(private val mRssFeedModels: List<RssFeedModel> //= MainActivity.RssClass.getFeedList();
) : RecyclerView.Adapter<Adapter.FeedModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): FeedModelViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return FeedModelViewHolder(v)
    }

    interface OnItemClickListener {
        fun onItemClick(item: RssFeedModel)
    }


    override fun onBindViewHolder(holder: FeedModelViewHolder, position: Int) {
        val rssFeedModel = mRssFeedModels[position]
        (holder.rssFeedView.findViewById<View>(R.id.item_number) as TextView).text = rssFeedModel.title
        (holder.rssFeedView.findViewById<View>(R.id.content) as TextView).text = rssFeedModel.description
        (holder.rssFeedView.findViewById<View>(R.id.url2) as TextView).text = rssFeedModel.link
        if (rssFeedModel.image !== "") {
            var `is`: InputStream? = null
            try {
                `is` = URL(rssFeedModel.image).content as InputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val draw = Drawable.createFromStream(`is`, "src name")
            holder.rssFeedView.findViewById<View>(R.id.item_image_view).background = draw
        }
        //((TextView)holder.rssFeedView.findViewById(R.id.item_number)).setText(R.string.test);
        //((TextView)holder.rssFeedView.findViewById(R.id.content)).setText(R.string.test);
        //((TextView)holder.rssFeedView.findViewById(R.id.url2)).setText(R.string.test);
    }

    override fun getItemCount(): Int {
        return mRssFeedModels.size
        //return 1;
    }

    class FeedModelViewHolder(val rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)
}

