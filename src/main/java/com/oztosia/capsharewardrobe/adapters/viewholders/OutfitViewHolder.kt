package com.oztosia.capsharewardrobe.adapters.viewholders

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.models.Outfit
import com.oztosia.capsharewardrobe.ui.outfitAsks.OutfitAskFragment


class OutfitViewHolder(val fragment: OutfitAskFragment, view: View)
    : GenericViewHolder<Outfit>(view) {

    val application = CapShareWardrobe.getAppContext()
    var imageView: ImageView = view.findViewById(R.id.image)

    override fun bind(item: Outfit){

        Glide.with(application)
            .load(item.downloadURL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)


        itemView.setOnClickListener {
            fragment.viewOutfit(item)
        }
    }
    }