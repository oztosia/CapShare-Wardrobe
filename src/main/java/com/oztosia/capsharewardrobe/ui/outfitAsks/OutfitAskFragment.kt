package com.oztosia.capsharewardrobe.ui.outfitAsks

import GenericAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oztosia.capsharewardrobe.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.adapters.viewholders.HashtagListViewHolder
import com.oztosia.capsharewardrobe.adapters.viewholders.PostImageViewHolder
import com.oztosia.capsharewardrobe.databinding.FragmentOutfitAskBinding
import com.oztosia.capsharewardrobe.utils.DateFormatter
import com.oztosia.capsharewardrobe.models.OutfitAsk
import com.oztosia.capsharewardrobe.models.Photo
import com.oztosia.capsharewardrobe.singletons.CreateOutfitItemListHolder.Singleton.outfitItemsList
import com.oztosia.capsharewardrobe.viewmodels.OutfitAskViewModel
import com.google.android.flexbox.*
import com.oztosia.capsharewardrobe.adapters.viewholders.OutfitViewHolder
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.models.Outfit
import kotlinx.coroutines.*
import me.relex.circleindicator.CircleIndicator2
import org.parceler.Parcels
import java.util.concurrent.TimeUnit

class OutfitAskFragment: Fragment() {

    private var _binding: FragmentOutfitAskBinding? = null
    private val binding get() = _binding!!
    private lateinit var outfitAsk: OutfitAsk
    lateinit var hashtagsAdapter: GenericAdapter<String>
    lateinit var imagesAdapter: GenericAdapter<Photo>
    lateinit var responsesAdapter: GenericAdapter<Outfit>
    private lateinit var outfitAsksViewModel: OutfitAskViewModel

    val application = CapShareWardrobe.getAppContext()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        outfitAsksViewModel = ViewModelProvider(requireActivity())[OutfitAskViewModel::class.java]

        _binding = FragmentOutfitAskBinding.inflate(inflater, container, false)

        outfitAsk = Parcels.unwrap(arguments?.getParcelable("outfitAsk"))

        if (outfitAsk.authorUid == FirebaseConst.CURRENT_USER) binding.moreIcon.visibility = View.VISIBLE

        val hashtagsLayoutManager = FlexboxLayoutManager(application)
        hashtagsLayoutManager.flexDirection = FlexDirection.ROW
        hashtagsLayoutManager.flexWrap = FlexWrap.WRAP
        hashtagsLayoutManager.justifyContent = JustifyContent.CENTER
        hashtagsLayoutManager.alignItems = AlignItems.STRETCH

        binding.hashtagsRecyclerview.layoutManager = hashtagsLayoutManager
        hashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) }, R.layout.item_hashtag)
        binding.hashtagsRecyclerview.adapter = hashtagsAdapter
        outfitAsksViewModel.getHashtagsDataFromFirestoreToRecyclerView(hashtagsAdapter, outfitAsk.id)

        val pagerSnapHelper = PagerSnapHelper()

        val imagesLayoutManager = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
        binding.imagesRecyclerView.layoutManager = imagesLayoutManager
        imagesAdapter = GenericAdapter({ PostImageViewHolder(it) }, R.layout.item_post_image)
        binding.imagesRecyclerView.adapter = imagesAdapter


        outfitAsksViewModel.getOutfitAskImagesFromFirestoreToRecyclerView(imagesAdapter, outfitAsk.id)


        val emptyObserver = object : RecyclerView.AdapterDataObserver() {
            @SuppressLint("ResourceAsColor")
            override fun onChanged() {
                val itemCount = imagesAdapter.itemCount
                val indicator: CircleIndicator2 = binding.indicator
                indicator.attachToRecyclerView(binding.imagesRecyclerView, pagerSnapHelper)
                indicator.createIndicators(itemCount,0)
                indicator.animatePageSelected(2)
                indicator.tintIndicator(R.color.black)
            }
        }
        imagesAdapter.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()

        val layoutManager = GridLayoutManager(CapShareWardrobe.getAppContext(), 2)
        binding.responsesRecyclerview.layoutManager = layoutManager
        responsesAdapter = GenericAdapter({ OutfitViewHolder(this@OutfitAskFragment,it) }, R.layout.item_outfit)
        binding.responsesRecyclerview.adapter = responsesAdapter

        outfitAsksViewModel.getResponses(outfitAsk.id, responsesAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moreIcon.setOnClickListener { binding.postMenuDrawer.visibility = View.VISIBLE }

        binding.postMenuDrawer.setNavigationItemSelectedListener {
            when (it.itemId) { R.id.nav_delete -> {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(2.5.toLong()))
                    outfitAsksViewModel.delete(outfitAsk.id)
                    withContext(Dispatchers.Main) {
                        activity?.onBackPressed()

                    }
                }
            }
            }
            return@setNavigationItemSelectedListener true
        }




        binding.title.text = outfitAsk.title

        if (outfitAsk.text.length < 100 )
        { binding.expandText.visibility = View.GONE
            binding.postText.text = outfitAsk.text}

        binding.expandText.setOnClickListener {
            binding.postText.maxLines = 100
            binding.expandText.visibility = View.GONE
        }

        binding.date.text = DateFormatter.convertFromMillisToDate(outfitAsk.date.toLong())

        binding.miniProfilePhotoImageView

        outfitAsksViewModel.getProfileImage(outfitAsk.authorUid, binding.miniProfilePhotoImageView, application)

        outfitAsksViewModel.getUserName(outfitAsk.authorUid, binding.userName)

        binding.addOutfitButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("userName", outfitAsk.authorUid)
            bundle.putString("id", outfitAsk.id)
            outfitItemsList.clear()
            for (image in imagesAdapter.dataSet)
            outfitItemsList.add(image.downloadURL)
            findNavController().navigate(R.id.action_OutfitAskFragment_to_OutfitCreateFragment, bundle)
        }
    }

    fun viewOutfit(outfit: Outfit){
        val bundle = Bundle()
        bundle.putParcelable("outfit", Parcels.wrap(outfit))
        findNavController().navigate(R.id.action_OutfitAskFragment_to_OutfitFragment, bundle)
    }



}