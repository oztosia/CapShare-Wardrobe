package com.example.mypersonalwardrobe.ui.outfitAsks


import GenericAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.HashtagListViewHolder
import com.example.mypersonalwardrobe.adapters.viewholders.PostImageViewHolder
import com.example.mypersonalwardrobe.databinding.FragmentOutfitAskBinding
import com.example.mypersonalwardrobe.utils.DateFormatter
import com.example.mypersonalwardrobe.models.OutfitAsk
import com.example.mypersonalwardrobe.models.Photo
import com.example.mypersonalwardrobe.viewmodels.OutfitAskViewModel
import com.google.android.flexbox.*
import me.relex.circleindicator.CircleIndicator2
import org.parceler.Parcels

class SingleOutfitAskFragment: Fragment() {


    private var _binding: FragmentOutfitAskBinding? = null
    private val binding get() = _binding!!
    private lateinit var outfitAsk: OutfitAsk
    lateinit var hashtagsAdapter: GenericAdapter<String>
    lateinit var imagesAdapter: GenericAdapter<Photo>
    private lateinit var outfitAsksViewModel: OutfitAskViewModel

    val application = MyPersonalWardrobe.getAppContext()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        outfitAsksViewModel = ViewModelProvider(requireActivity())[OutfitAskViewModel::class.java]

        _binding = FragmentOutfitAskBinding.inflate(inflater, container, false)

        outfitAsk = Parcels.unwrap(arguments?.getParcelable("outfitAsk"))


        val hashtagsLayoutManager = FlexboxLayoutManager(application)
        hashtagsLayoutManager.flexDirection = FlexDirection.ROW
        hashtagsLayoutManager.flexWrap = FlexWrap.WRAP
        hashtagsLayoutManager.justifyContent = JustifyContent.CENTER
        hashtagsLayoutManager.alignItems = AlignItems.STRETCH


        binding.hashtagsRecyclerview.layoutManager = hashtagsLayoutManager
        hashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) },
            R.layout.item_hashtag)
        binding.hashtagsRecyclerview.adapter = hashtagsAdapter
        outfitAsksViewModel
            .getHashtagsDataFromFirestoreToRecyclerView(hashtagsAdapter,
                outfitAsk.id)


        val pagerSnapHelper = PagerSnapHelper()


        val imagesLayoutManager = LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false)
        binding.imagesRecyclerView.layoutManager = imagesLayoutManager
        imagesAdapter = GenericAdapter({ PostImageViewHolder(it) }, R.layout.post_image)
        binding.imagesRecyclerView.adapter = imagesAdapter


        outfitAsksViewModel
            .getOutfitAskImagesFromFirestoreToRecyclerView(
                imagesAdapter,
                outfitAsk.id)


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

        if (binding.postText.text.length < 200){
            binding.expandText.visibility = View.GONE
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = outfitAsk.title

        binding.postText.text = outfitAsk.text

        binding.expandText.setOnClickListener {
            binding.postText.maxLines = 100
            binding.expandText.visibility = View.GONE
        }

        binding.date.text = DateFormatter.convertFromMillisToDate(outfitAsk.date.toLong())

        binding.miniProfilePhotoImageView

        outfitAsksViewModel.getProfileImage(outfitAsk.authorUid, binding.miniProfilePhotoImageView, application)

        outfitAsksViewModel.getUserName(outfitAsk.authorUid, binding.userName)

        binding.addOutfitButton.setOnClickListener{
            //findNavController().navigate(R.id.action_SingleOutfitAskFragment_to_CreateOutfitFragment)
        }
    }


}