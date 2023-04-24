package com.example.mypersonalwardrobe.adapters.viewholders

import GenericAdapter
import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.utils.DateFormatter
import com.example.mypersonalwardrobe.models.OutfitAsk
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.ui.outfitAsks.AskForOutfitFragment
import com.example.mypersonalwardrobe.viewmodels.OutfitAskViewModel
import me.relex.circleindicator.CircleIndicator2


class OutfitAskViewHolder(val fragment: AskForOutfitFragment, view: View)
    : GenericViewHolder<OutfitAsk>(view) {

    val application = MyPersonalWardrobe.getAppContext()

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
        imagesAdapter = GenericAdapter({ PostImageViewHolder(it) }, R.layout.post_image)


        outfitAskViewModel
            .getOutfitAskImagesFromFirestoreToRecyclerView(
                imagesAdapter,
                item.id)



        val imagesLayoutManager = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
        postImageRecyclerAdapter.layoutManager = imagesLayoutManager
        postImageRecyclerAdapter.adapter = imagesAdapter




        val emptyObserver = object : RecyclerView.AdapterDataObserver() {
            @SuppressLint("ResourceAsColor")
            override fun onChanged() {
                val itemCount = imagesAdapter.itemCount
                val indicator: CircleIndicator2 = view.findViewById(R.id.indicator)
                indicator.attachToRecyclerView(postImageRecyclerAdapter, pagerSnapHelper)
                indicator.createIndicators(itemCount,0)
                indicator.animatePageSelected(2)
                indicator.tintIndicator(R.color.black)
            }
        }
        imagesAdapter.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()

        itemView.setOnClickListener {
            fragment.viewSingleOutfitAsk(item)
        }


    }


}