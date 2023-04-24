package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R

class ItemToPostOrAskViewHolder(view: View)
: GenericViewHolder<String>(view) {

    val application = MyPersonalWardrobe.getAppContext()

    var recyclerImageView: ImageView
    var deleteIcon: ImageView
    var emptyCheckIcon: ImageView
    var checkIcon: ImageView

    init {
        recyclerImageView = view.findViewById(R.id.recyclerImageView)
        deleteIcon = view.findViewById(R.id.delete_image_icon)
        emptyCheckIcon = view.findViewById(R.id.empty_check_icon)
        checkIcon = view.findViewById(R.id.check_icon)
    }

    override fun bind(item: String) {

        emptyCheckIcon.visibility = View.GONE
        checkIcon.visibility = View.GONE

        Glide.with(application).load(item).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(recyclerImageView)

        deleteIcon.setOnClickListener {
            //fragment.removeItem(item) TODO jeden fragment outfitasks i posts
        }

    }
}
