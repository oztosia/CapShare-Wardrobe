package com.oztosia.capsharewardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.models.User
import com.oztosia.capsharewardrobe.ui.usersList.UsersListFragment

class UsersListViewHolder(view: View,
                          val fragment: UsersListFragment)
    : GenericViewHolder<User>(view){

    val application = CapShareWardrobe.getAppContext()
    var recyclerImageView: ImageView
    var recyclerTextView: TextView

    init {
        recyclerImageView = view.findViewById(R.id.profilePhotoImageView)
        recyclerTextView = view.findViewById(R.id.user_name)
    }

    override fun bind(item: User) {

        recyclerTextView.text = item.userName

        Glide.with(application).load(item.profileImage)
            .centerCrop()
            .placeholder(R.drawable.icon)
            .into(recyclerImageView)


        recyclerImageView.setOnClickListener {
            fragment.viewProfile(item)
        }
    }
}