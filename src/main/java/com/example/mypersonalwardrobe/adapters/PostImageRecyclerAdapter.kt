package com.example.mypersonalwardrobe.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.firebase.FirebasePostsRepo
import com.example.mypersonalwardrobe.ui.addPost.AddPostFragment
import com.example.mypersonalwardrobe.viewmodels.SharedHomeAndGalleryViewModel

class PostImageRecyclerAdapter(
    val fragment: AddPostFragment,
    private val imagesArray: ArrayList<Uri>
) : RecyclerView.Adapter<PostImageRecyclerAdapter.PostHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PostHolder(v)
    }

    override fun getItemCount(): Int = imagesArray.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(imagesArray[position])
    }

    inner class PostHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(uri: Uri){
            var postViewModel = SharedHomeAndGalleryViewModel()
            var recyclerImageView: ImageView = view.findViewById(R.id.recyclerImageView)
            var deleteImageIcon: ImageView = view.findViewById(R.id.delete_image_icon)

            Glide.with(view.context).load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(recyclerImageView)

            deleteImageIcon.setOnClickListener {
                imagesArray.remove(uri)
                fragment.removeItemFromOriginalArrayList(uri)
                notifyItemRemoved(position);
            }


        }

    }

}