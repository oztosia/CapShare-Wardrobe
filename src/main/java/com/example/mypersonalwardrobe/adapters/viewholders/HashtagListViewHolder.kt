package com.example.mypersonalwardrobe.adapters.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.Photo

class HashtagListViewHolder(view: View)
    : GenericViewHolder<String>(view) {

    val application: Context = MyPersonalWardrobe.getAppContext()

    var hashtagTextView: TextView = view.findViewById(R.id.hashtag_item)

    override fun bind(item: String) {

        hashtagTextView.text = item

    }
}
