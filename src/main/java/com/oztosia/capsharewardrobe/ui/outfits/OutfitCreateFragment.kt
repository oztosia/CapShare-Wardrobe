package com.oztosia.capsharewardrobe.ui.outfits

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.databinding.FragmentOutfitCreateBinding
import com.oztosia.capsharewardrobe.interfaces.BitmapToUriConverter
import com.oztosia.capsharewardrobe.interfaces.ViewToBitmapConverter
import com.oztosia.capsharewardrobe.singletons.CreateOutfitItemListHolder.Singleton.outfitItemsList
import com.oztosia.capsharewardrobe.viewmodels.OutfitsViewModel
import com.oztosia.capsharewardrobe.views.CreateOutfitView

class OutfitCreateFragment : Fragment(){

    private var _binding: FragmentOutfitCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var uid: String
    private lateinit var id: String
    private lateinit var createOutfitView: CreateOutfitView
    private lateinit var outfitsViewModel: OutfitsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        outfitsViewModel = ViewModelProvider(requireActivity()).get(OutfitsViewModel::class.java)
        _binding = FragmentOutfitCreateBinding.inflate(inflater, container, false)

        uid = arguments?.getString("userName")!!
        id = arguments?.getString("id")!!

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createOutfitView = CreateOutfitView(requireContext(), outfitItemsList)
        rescaleView(createOutfitView)


        binding.addButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userName", uid)
            bundle.putString("action", "createOutfit")
            findNavController().navigate(R.id.action_OutfitCreateFragment_to_GalleryFragment, bundle)
        }

        binding.removeButton.setOnClickListener {
            val last = createOutfitView.bitmaps.lastIndex
            outfitItemsList.removeAt(last)
            createOutfitView.bitmaps.removeAt(last)
            createOutfitView.bitmapsPositions.removeAt(last)
            createOutfitView.topBitmapIndex = -1
            createOutfitView.invalidate()
        }

        binding.confirmButton.setOnClickListener {
            val bitmap = ViewToBitmapConverter.viewToBitmap(binding.canva)
            val uri = BitmapToUriConverter.bitmapToUri(bitmap, requireActivity())
            outfitsViewModel.addOutfit(uri, uid, FirebaseConst.CURRENT_USER, id, binding.progressBar)

        }


    }


    fun rescaleView(view: View){
        val targetWidthPx = 720
        val targetHeightPx = 1449

        view.id = View.generateViewId()

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.layoutParams = layoutParams

        binding.canva.addView(view)

        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val screenWidthPx = resources.displayMetrics.widthPixels
                val screenHeightPx = resources.displayMetrics.heightPixels

                val scaleX = screenWidthPx.toFloat() / targetWidthPx
                val scaleY = screenHeightPx.toFloat() / targetHeightPx

                view.scaleX = scaleX
                view.scaleY = scaleY
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}