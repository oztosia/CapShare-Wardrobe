package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.utils.ItemsListHolder.ItemsListHolder.addItem
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.ui.gallery.GalleryFragment
import com.example.mypersonalwardrobe.viewmodels.OutfitAskViewModel

class ItemsListViewHolder (val fragment: GalleryFragment, view: View)
    : GenericViewHolder<Photo>(view) {

    val application = MyPersonalWardrobe.getAppContext()
    val outfitAskViewModel: OutfitAskViewModel = OutfitAskViewModel()

    var recyclerImageView: ImageView
    var deleteIcon: ImageView
    var emptyCheckIcon: ImageView
    var checkIcon: ImageView

    init {
        recyclerImageView = view.findViewById(R.id.recyclerImageView)
        deleteIcon = view.findViewById(R.id.delete_image_icon)
        emptyCheckIcon = view.findViewById(R.id.empty_check_icon)
        checkIcon = view.findViewById(R.id.check_icon)
        deleteIcon = view.findViewById(R.id.delete_image_icon)
    }

    override fun bind(item: Photo) {

        deleteIcon.visibility = View.GONE
        emptyCheckIcon.visibility = View.GONE
        checkIcon.visibility = View.GONE

        if (fragment.arguments?.getString("action") == "create"){
            emptyCheckIcon.visibility = View.VISIBLE
        }

        emptyCheckIcon.setOnClickListener {
            addItem(item.downloadURL)
            checkIcon.visibility = View.VISIBLE
            emptyCheckIcon.visibility = View.GONE
        }

        checkIcon.setOnClickListener {
            outfitAskViewModel.deleteImageUriFromPhotoList(item.downloadURL)
            emptyCheckIcon.visibility = View.VISIBLE
            checkIcon.visibility = View.GONE
        }

        Glide.with(application)
            .load(item.downloadURL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(recyclerImageView)

        recyclerImageView.setOnClickListener {
            fragment.showImage(position)
        }
    }
}
