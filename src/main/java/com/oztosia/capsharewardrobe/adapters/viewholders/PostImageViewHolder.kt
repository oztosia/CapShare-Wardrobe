package com.oztosia.capsharewardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.models.Photo


class PostImageViewHolder (view: View)
    : GenericViewHolder<Photo>(view) {

    val application = CapShareWardrobe.getAppContext()

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
