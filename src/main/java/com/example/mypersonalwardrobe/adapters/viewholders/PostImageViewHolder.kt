package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.PostImage


class PostImageViewHolder (view: View)
    : GenericViewHolder<PostImage>(view) {

    val application = MyPersonalWardrobe.getAppContext()

    var recyclerImageView: ImageView
    var deleteIcon: ImageView

    init {
        recyclerImageView = view.findViewById(R.id.recyclerImageView)
        deleteIcon = view.findViewById(R.id.delete_image_icon)
    }

    override fun bind(item: PostImage) {

        deleteIcon.visibility = View.GONE

        Glide.with(application).load(item.downloadURL.toUri())
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(recyclerImageView)


        recyclerImageView.setOnClickListener {
            //fragment.viewSinglePhoto(item, adapterPosition)
        }




    }
}
