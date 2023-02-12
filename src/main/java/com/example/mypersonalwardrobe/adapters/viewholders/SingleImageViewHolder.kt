package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.ui.gallery.GalleryFragment
import com.example.mypersonalwardrobe.viewmodels.GalleryViewModel
import com.google.android.material.navigation.NavigationView

class SingleImageViewHolder  (val fragment: GalleryFragment,view: View)
    : GenericViewHolder<Photo>(view) {

    val application = MyPersonalWardrobe.getAppContext()

    var recyclerImageView: ImageView = view.findViewById(R.id.image)
    var hashtags: TextView = view.findViewById(R.id.hashtags_textView)
    var options: ImageView = view.findViewById(R.id.options)
    var photoMenuDrawer: NavigationView = view.findViewById(R.id.photo_menu_drawer)



    override fun bind(item: Photo) {


        if (fragment.arguments?.getString("userName") != FirebasePathsConstants.CURRENT_USER){
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
