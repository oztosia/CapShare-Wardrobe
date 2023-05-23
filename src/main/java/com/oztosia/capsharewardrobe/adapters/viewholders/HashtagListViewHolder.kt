package com.oztosia.capsharewardrobe.adapters.viewholders

import android.view.View
import android.widget.TextView
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.models.Message

class HashtagListViewHolder(view: View) : GenericViewHolder<String>(view) {

    var hashtagTextView: TextView = view.findViewById(R.id.hashtag_item)

    override fun bind(item: String) { hashtagTextView.text = item }

}
