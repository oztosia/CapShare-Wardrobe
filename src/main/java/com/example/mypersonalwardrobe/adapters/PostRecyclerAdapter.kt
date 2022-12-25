package com.example.mypersonalwardrobe.adapters

import com.example.mypersonalwardrobe.models.Post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R



class PostRecyclerAdapter(val data: ArrayList<Post>)
    :RecyclerView.Adapter<PostRecyclerAdapter.MyViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)

        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }


    inner class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(post: Post){

            var profileImageView: ImageView = view.findViewById(R.id.profilePhotoImageView)
            var userNameTextView: TextView = view.findViewById(R.id.user_name)
            var dateTextView: TextView = view.findViewById(R.id.date)
            var hashtagsRecyclerAdapter: RecyclerView = view.findViewById(R.id.hashtags_recyclerview)
            var postTextView: TextView = view.findViewById(R.id.post_text)
            var postImageRecyclerAdapter: RecyclerView = view.findViewById(R.id.images_recyclerView)
            var likesTextView: TextView = view.findViewById(R.id.likes)



            //userNameTextView.text = post.userName
            dateTextView.text = post.date.toString()

            postTextView.text = post.text

            likesTextView.text = post.likes.toString()
/*

            Glide.with(view.context).load(post.imagesList)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(postImageRecyclerAdapter)

            likesTextView.setOnClickListener {
                fragment.like(post)
            }

 */
        }


    }
}