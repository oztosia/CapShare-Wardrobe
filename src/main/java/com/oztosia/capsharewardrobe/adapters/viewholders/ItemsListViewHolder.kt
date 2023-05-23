package com.oztosia.capsharewardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.models.Photo
import com.oztosia.capsharewardrobe.singletons.CreateOutfitItemListHolder.Singleton.outfitItemsList
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder.Singleton.list
import com.oztosia.capsharewardrobe.ui.gallery.GalleryFragment

class ItemsListViewHolder (val fragment: GalleryFragment, view: View)
    : GenericViewHolder<Photo>(view) {

    val application = CapShareWardrobe.getAppContext()
    var itemsList = mutableListOf<String>()


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
            itemsList = list
        }
        else if (fragment.arguments?.getString("action") == "createOutfit"){
            emptyCheckIcon.visibility = View.VISIBLE
            itemsList = outfitItemsList
        }

        emptyCheckIcon.setOnClickListener {
            itemsList.add(item.downloadURL)
            checkIcon.visibility = View.VISIBLE
            emptyCheckIcon.visibility = View.GONE
        }

        checkIcon.setOnClickListener {
            itemsList.remove(item.downloadURL)
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
