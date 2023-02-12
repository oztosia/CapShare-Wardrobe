package com.example.mypersonalwardrobe.adapters.viewholders

import GenericAdapter
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.OutfitAsk
import com.example.mypersonalwardrobe.models.PostImage
import com.example.mypersonalwardrobe.ui.outfits.AddOutfitAskFragment
import com.example.mypersonalwardrobe.ui.outfits.AskForOutfitFragment
import com.example.mypersonalwardrobe.viewmodels.OutfitAskViewModel
import me.relex.circleindicator.CircleIndicator2


class OutfitAskViewHolder(val fragment: AskForOutfitFragment, view: View)
    : GenericViewHolder<OutfitAsk>(view) {

    val application = MyPersonalWardrobe.getAppContext()

    lateinit var imagesAdapter: GenericAdapter<PostImage>

    val outfitAskViewModel: OutfitAskViewModel = OutfitAskViewModel()


    lateinit var title: TextView
    var dateTextView: TextView = view.findViewById(R.id.date)
    var postImageRecyclerAdapter: RecyclerView = view.findViewById(R.id.images_recyclerView)


    override fun bind(item: OutfitAsk){

        title = view.findViewById(R.id.title)

        dateTextView.text = item.date
        title.text = item.title


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
            override fun onChanged() {
                val itemCount = imagesAdapter.itemCount
                val indicator: CircleIndicator2 = view.findViewById(R.id.indicator)
                indicator.attachToRecyclerView(postImageRecyclerAdapter, pagerSnapHelper)
                indicator.createIndicators(itemCount,0)
                indicator.animatePageSelected(2)

                Log.d(TAG, "indicator " + itemCount)
            }
        }
        imagesAdapter.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()

        postImageRecyclerAdapter.addItemDecoration(DividerItemDecoration(application, 0))

        title.setOnClickListener {
            fragment.viewSingleOutfitAsk(item)
        }


    }


}