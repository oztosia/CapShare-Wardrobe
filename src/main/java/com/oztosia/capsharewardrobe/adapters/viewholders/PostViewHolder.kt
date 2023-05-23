package com.oztosia.capsharewardrobe.adapters.viewholders

import GenericAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.utils.DateFormatter
import com.oztosia.capsharewardrobe.models.Photo
import com.oztosia.capsharewardrobe.models.Post
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.viewmodels.PostsViewModel
import com.google.android.flexbox.*
import com.google.android.material.navigation.NavigationView
import com.oztosia.capsharewardrobe.models.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator2

class PostViewHolder(view: View)
    : GenericViewHolder<Post>(view) {

    val application = CapShareWardrobe.getAppContext()!!

    lateinit var imagesAdapter: GenericAdapter<Photo>
    lateinit var hashtagsAdapter: GenericAdapter<String>

    private val postViewModel: PostsViewModel = PostsViewModel()

        lateinit var profileImageView: ImageView
        lateinit var userNameTextView: TextView
        var dateTextView: TextView = view.findViewById(R.id.date)
        var moreIcon: ImageView = view.findViewById(R.id.more_icon)
        var postMenuDrawer: NavigationView = view.findViewById(R.id.post_menu_drawer)
        var hashtagsRecyclerAdapter: RecyclerView = view.findViewById(R.id.hashtags_recyclerview)
        var postTextView: TextView = view.findViewById(R.id.post_text)
        var postImageRecyclerAdapter: RecyclerView = view.findViewById(R.id.images_recyclerView)
        var expandTextView: TextView = view.findViewById(R.id.expand_text)
        var likeIcon: ImageView = view.findViewById(R.id.like_icon)
        var unlikeIcon: ImageView = view.findViewById(R.id.unlike_icon)
        var likesTextView: TextView = view.findViewById(R.id.likes)

    override fun bind(item: Post){

      profileImageView = view.findViewById(R.id.miniProfilePhotoImageView)
        postViewModel.getProfileImage(item.authorUid, profileImageView, application)

        userNameTextView =  view.findViewById(R.id.user_name)
        postViewModel.getUserName(item.authorUid, userNameTextView)

        dateTextView.text = DateFormatter.convertFromMillisToDate(item.date.toLong())

        val hashtagsLayoutManager = FlexboxLayoutManager(hashtagsRecyclerAdapter.context)
        hashtagsLayoutManager.flexDirection = FlexDirection.ROW
        hashtagsLayoutManager.flexWrap = FlexWrap.WRAP
        hashtagsLayoutManager.justifyContent = JustifyContent.CENTER
        hashtagsLayoutManager.alignItems = AlignItems.STRETCH
        hashtagsRecyclerAdapter.layoutManager = hashtagsLayoutManager
        hashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) }, R.layout.item_hashtag)
        hashtagsRecyclerAdapter.adapter = hashtagsAdapter
        postViewModel.getHashtagsDataFromFirestoreToRecyclerView(hashtagsAdapter, item.id)

        if (item.authorUid == FirebaseConst.CURRENT_USER) moreIcon.visibility = View.VISIBLE

        moreIcon.setOnClickListener { postMenuDrawer.visibility = View.VISIBLE }

        postMenuDrawer.setNavigationItemSelectedListener {
            when (it.itemId) { R.id.nav_delete -> { CoroutineScope(Dispatchers.IO).launch { postViewModel.delete(item.id) } } }
            return@setNavigationItemSelectedListener true
        }

        postTextView.text = item.text

        if (postTextView.text.length < 100){ expandTextView.visibility = View.GONE }

        expandTextView.setOnClickListener {
            postTextView.maxLines = 100
            expandTextView.visibility = View.GONE
        }

        imagesAdapter = GenericAdapter({ PostImageViewHolder(it) }, R.layout.item_post_image)
        postViewModel.getPostsImagesFromFirestoreToRecyclerView(imagesAdapter, item.id)
        val imagesLayoutManager = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
        postImageRecyclerAdapter.layoutManager = imagesLayoutManager
        postImageRecyclerAdapter.adapter = imagesAdapter
        val pagerSnapHelper = PagerSnapHelper()
        val emptyObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                val itemCount = imagesAdapter.itemCount
                val indicator: CircleIndicator2 = view.findViewById(R.id.indicator)
                indicator.attachToRecyclerView(postImageRecyclerAdapter, pagerSnapHelper)
                indicator.createIndicators(itemCount,0)
                indicator.animatePageSelected(2)
            }
        }
        imagesAdapter.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()

        postViewModel.checkIfPostIsLiked(item.id, likeIcon, unlikeIcon)

        postViewModel.getLikesFromFirestore(item.id, likesTextView)

        likeIcon.setOnClickListener { postViewModel.like(item.id, likesTextView, likeIcon, unlikeIcon) }

        unlikeIcon.setOnClickListener { postViewModel.unlike(item.id, likesTextView, likeIcon, unlikeIcon) }
    }
}