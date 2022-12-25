package com.example.mypersonalwardrobe.adapters.viewholders


import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class GenericViewHolder<T>(val view: View)
    : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T)


}
