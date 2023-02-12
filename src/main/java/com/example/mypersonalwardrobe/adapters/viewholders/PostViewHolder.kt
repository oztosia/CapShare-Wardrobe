package com.example.mypersonalwardrobe.adapters.viewholders

import GenericAdapter
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.models.Post
import com.example.mypersonalwardrobe.models.PostImage
import com.example.mypersonalwardrobe.viewmodels.PostsViewModel
import com.google.android.flexbox.*
import me.relex.circleindicator.CircleIndicator2



class PostViewHolder(view: View)
    : GenericViewHolder<Post>(view) {

    val application = MyPersonalWardrobe.getAppContext()

    lateinit var imagesAdapter: GenericAdapter<PostImage>
    lateinit var hashtagsAdapter: GenericAdapter<String>

    val postViewModel: PostsViewModel = PostsViewModel()


    lateinit var profileImageView: ImageView
    lateinit var userNameTextView: TextView
        var dateTextView: TextView = view.findViewById(R.id.date)
        var hashtagsRecyclerAdapter: RecyclerView = view.findViewById(R.id.hashtags_recyclerview)
        var postTextView: TextView = view.findViewById(R.id.post_text)
        var postImageRecyclerAdapter: RecyclerView = view.findViewById(R.id.images_recyclerView)
        var expandTextView: TextView = view.findViewById(R.id.expand_text)
        var loveIcon: ImageView = view.findViewById(R.id.love_icon)
        var likesTextView: TextView = view.findViewById(R.id.likes)




    override fun bind(item: Post){

        postTextView.text = item.text


        if (postTextView.text.length < 100){
            expandTextView.visibility = View.GONE
        }

        profileImageView = view.findViewById(R.id.miniProfilePhotoImageView)
        postViewModel.getProfileImage(item.authorUid, profileImageView)




        userNameTextView =  view.findViewById(R.id.user_name)
        postViewModel.getUserName(item.authorUid, userNameTextView)



        val hashtagsLayoutManager = FlexboxLayoutManager(hashtagsRecyclerAdapter.context)
        hashtagsLayoutManager.flexDirection = FlexDirection.ROW
        hashtagsLayoutManager.flexWrap = FlexWrap.WRAP
        hashtagsLayoutManager.justifyContent = JustifyContent.CENTER
        hashtagsLayoutManager.alignItems = AlignItems.STRETCH






        hashtagsRecyclerAdapter.layoutManager = hashtagsLayoutManager
        hashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) },
            R.layout.hashtag_item)
        hashtagsRecyclerAdapter.adapter = hashtagsAdapter
        postViewModel
            .getHashtagsDataFromFirestoreToRecyclerView(hashtagsAdapter,
                item.id)



        dateTextView.text = item.date





        expandTextView.setOnClickListener {
            postTextView.maxLines = 100
            expandTextView.visibility = View.GONE
        }


        val pagerSnapHelper = PagerSnapHelper()
        imagesAdapter = GenericAdapter({ PostImageViewHolder(it) }, R.layout.post_image)


        postViewModel
            .testPostsImagesFromFirestoreToRecyclerView(
                imagesAdapter,
                item.authorUid,
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



        //likesTextView.text = item.likes.toString()

        loveIcon.setOnClickListener {

        }



    }


}