package com.oztosia.capsharewardrobe.adapters.viewholders


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.oztosia.capsharewardrobe.models.Message

abstract class GenericViewHolder<T>(val view: View)
    : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T)
}
