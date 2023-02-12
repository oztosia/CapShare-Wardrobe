package com.example.mypersonalwardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.User
import com.example.mypersonalwardrobe.ui.usersList.UsersListFragment

class UsersListViewHolder(view: View,
                          val fragment: UsersListFragment)
    : GenericViewHolder<User>(view){

    val application = MyPersonalWardrobe.getAppContext()
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
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(recyclerImageView)


        recyclerImageView.setOnClickListener {
            fragment.viewProfile(item)
        }
    }
}