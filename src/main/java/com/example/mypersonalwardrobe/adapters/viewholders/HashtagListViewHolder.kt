package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.TextView
import com.example.mypersonalwardrobe.R

class HashtagListViewHolder(view: View) : GenericViewHolder<String>(view) {

    var hashtagTextView: TextView = view.findViewById(R.id.hashtag_item)

    override fun bind(item: String) { hashtagTextView.text = item }

}
