package com.oztosia.capsharewardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.models.Photo
import com.oztosia.capsharewardrobe.ui.gallery.GalleryFragment
import com.google.android.material.navigation.NavigationView
import com.oztosia.capsharewardrobe.models.Message

class SingleImageViewHolder  (val fragment: GalleryFragment,view: View)
    : GenericViewHolder<Photo>(view) {

    val application = CapShareWardrobe.getAppContext()

    var recyclerImageView: ImageView = view.findViewById(R.id.image)
    var hashtags: TextView = view.findViewById(R.id.hashtags_textView)
    var options: ImageView = view.findViewById(R.id.options)
    var photoMenuDrawer: NavigationView = view.findViewById(R.id.photo_menu_drawer)



    override fun bind(item: Photo) {


        if (fragment.arguments?.getString("userName") != FirebaseConst.CURRENT_USER){
            options.visibility = View.GONE
        }



        photoMenuDrawer.visibility = View.GONE

        hashtags.text = item.hashtags

        Glide.with(application)
            .load(item.downloadURL)
            .into(recyclerImageView)


        options.setOnClickListener {
            photoMenuDrawer.visibility = View.VISIBLE
        }

        photoMenuDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_delete -> {
                  fragment.delete(item)
                }
            }
            return@setNavigationItemSelectedListener true
        }


        recyclerImageView.setOnClickListener {
            photoMenuDrawer.visibility = View.GONE
            if (hashtags.isVisible) {
                hashtags.visibility = View.GONE
            }else {
                hashtags.visibility = View.VISIBLE
            }}




    }
}
