package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.Photo


class PostImageViewHolder (view: View)
    : GenericViewHolder<Photo>(view) {

    val application = MyPersonalWardrobe.getAppContext()

    var recyclerImageView: ImageView
    var deleteIcon: ImageView

    init {
        recyclerImageView = view.findViewById(R.id.recyclerImageView)
        deleteIcon = view.findViewById(R.id.delete_image_icon)
    }

    override fun bind(item: Photo) {

        deleteIcon.visibility = View.GONE


            Glide.with(application).load(item.downloadURL)
                .into(recyclerImageView)

        recyclerImageView.setOnClickListener {
                val bundle = bundleOf("uri" to item.downloadURL)
                Navigation.findNavController(it).navigate(R.id.SingleImageFragment, bundle)
        }




    }
}
