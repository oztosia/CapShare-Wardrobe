package com.example.mypersonalwardrobe.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.ui.items.GalleryFragment


class GalleryRecyclerAdapter(
    val fragment: GalleryFragment,
    private val imagesArray: ArrayList<Photo>
) : RecyclerView.Adapter<GalleryRecyclerAdapter.PostHolder>(), Filterable{

    var sortedArray: ArrayList<Photo> = arrayListOf()

    init {
        sortedArray = imagesArray
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PostHolder(v)
    }

    override fun getItemCount(): Int = sortedArray.size

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(sortedArray[position])
    }

    fun notifyAboutRemove(photo: Photo){
        sortedArray.remove(photo)
        notifyDataSetChanged()
    }

    inner class PostHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(photo: Photo){

            var recyclerImageView: ImageView = view.findViewById(R.id.recyclerImageView)
            var deleteIcon: ImageView = view.findViewById(R.id.delete_image_icon)

            deleteIcon.visibility = View.GONE

            Glide.with(view.context).load(photo.uri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(recyclerImageView)


            recyclerImageView.setOnClickListener {
                fragment.viewSinglePhoto(photo, adapterPosition)
            }


        }

    }




    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) sortedArray = imagesArray else {
                    val filteredList = ArrayList<Photo>()
                    imagesArray
                        .filter {
                            (it.hashtags.contains(constraint!!))
                        }
                        .forEach { filteredList.add(it) }
                    sortedArray = filteredList

                    Log.d(TAG, "filtered list size: " + sortedArray.size)

                }
                return FilterResults().apply { values = sortedArray }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                sortedArray = if (results?.values == null)
                    imagesArray
                else
                    results.values as ArrayList<Photo>
                notifyDataSetChanged()
            }
        }
    }

}