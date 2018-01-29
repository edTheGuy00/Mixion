package com.taskail.mixion.steemdiscussion

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.taskail.mixion.R
import kotlinx.android.synthetic.main.item_image.view.*

/**
 *Created by ed on 1/27/18.
 */
class DiscussionImagesAdapter(images: List<String>) :
        RecyclerView.Adapter<DiscussionImagesAdapter.ImageViewHolder>() {

    var images: List<String> = images

     set(loadedImages) {
         field = loadedImages
         notifyDataSetChanged()
     }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun setImage(imgUrl: String){

            with(imgUrl){
                Glide.with(itemView.rootView)
                        .load(imgUrl)
                        .into(itemView.image)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DiscussionImagesAdapter.ImageViewHolder {

        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_image,
                parent, false)

        return DiscussionImagesAdapter.ImageViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: DiscussionImagesAdapter.ImageViewHolder?, position: Int) {
        holder?.setImage(images[position])
    }
}