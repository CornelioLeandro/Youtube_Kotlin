package com.leandro.youtube

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_video.view.*

class VideoDetailAdapter(private val videos:List<Video>) : RecyclerView.Adapter<VideoDetailAdapter.VideoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder =
       VideoHolder(LayoutInflater.from(parent.context).inflate
           (R.layout.video_detail_list_item_video,parent,false))

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
     holder.bind(videos[position])
    }

    override fun getItemCount(): Int  = videos.size

    inner class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(video: Video) = with(itemView){
            Picasso.get().load(video.thumbnailUrl).into(video_thumbmail)
            video_title.text = video.title
            video_info.text = context.getString(R.string.info,
            video.publisher.name, video.viewsCountLabel, video.publishedAt.formatted())
        }
    }
}