package com.oztosia.capsharewardrobe.adapters.viewholders

import GenericAdapter
import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.utils.DateFormatter
import com.oztosia.capsharewardrobe.models.OutfitAsk
import com.oztosia.capsharewardrobe.models.Photo
import com.oztosia.capsharewardrobe.ui.outfits.OutfitsFragment
import com.oztosia.capsharewardrobe.viewmodels.OutfitAskViewModel
import me.relex.circleindicator.CircleIndicator2


class OutfitAskViewHolder(val fragment: OutfitsFragment, view: View)
    : GenericViewHolder<OutfitAsk>(view) {

    val application = CapShareWardrobe.getAppContext()

    lateinit var imagesAdapter: GenericAdapter<Photo>

    val outfitAskViewModel: OutfitAskViewModel = OutfitAskViewModel()


    lateinit var title: TextView
    var dateTextView: TextView = view.findViewById(R.id.date)
    var postImageRecyclerAdapter: RecyclerView = view.findViewById(R.id.images_recyclerView)


    override fun bind(item: OutfitAsk){

        title = view.findViewById(R.id.title)

        title.text = item.title

        val timestamp = DateFormatter.convertFromMillisToDate(item.date.toLong())
        dateTextView.text = timestamp

        val pagerSnapHelper = PagerSnapHelper()
        imagesAdapter = GenericAdapter({ PostImageViewHolder(it) }, R.layout.item_post_image)

        outfitAskViewModel.getOutfitAskImagesFromFirestoreToRecyclerView(imagesAdapter, item.id)

        val imagesLayoutManager = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
        postImageRecyclerAdapter.layoutManager = imagesLayoutManager
        postImageRecyclerAdapter.adapter = imagesAdapter


        val emptyObserver = object : RecyclerView.AdapterDataObserver() {
            @SuppressLint("ResourceAsColor")
            override fun onChanged() {
                var itemCount = imagesAdapter.itemCount
                val indicator: CircleIndicator2 = view.findViewById(R.id.indicator)
                indicator.attachToRecyclerView(postImageRecyclerAdapter, pagerSnapHelper)
                if (itemCount > 5) itemCount = 5
                indicator.createIndicators(itemCount,0)
                indicator.animatePageSelected(2)
                indicator.tintIndicator(R.color.black)
            }
        }
        imagesAdapter.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()

        itemView.setOnClickListener {
            fragment.viewOutfitAsk(item)
        }


    }


}