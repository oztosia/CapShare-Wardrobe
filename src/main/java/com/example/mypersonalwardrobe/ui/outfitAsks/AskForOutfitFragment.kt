package com.example.mypersonalwardrobe.ui.outfitAsks

import GenericAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.OutfitAskViewHolder
import com.example.mypersonalwardrobe.databinding.FragmentBaseRecyclerviewWithSearchBinding
import com.example.mypersonalwardrobe.models.OutfitAsk
import com.example.mypersonalwardrobe.viewmodels.OutfitAskViewModel
import org.parceler.Parcels


class AskForOutfitFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var outfitAsksViewModel: OutfitAskViewModel

    private var _binding: FragmentBaseRecyclerviewWithSearchBinding? = null
    private val binding get() = _binding!!

    lateinit var outfitAsksAdapter: GenericAdapter<OutfitAsk>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        outfitAsksViewModel = ViewModelProvider(requireActivity())[OutfitAskViewModel::class.java]

        _binding = FragmentBaseRecyclerviewWithSearchBinding.inflate(inflater, container, false)

        outfitAsksViewModel._lastDocumentSnapshotMutableLiveData.value = null

        val postLayoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        binding.recyclerView.layoutManager = postLayoutManager
        outfitAsksAdapter = GenericAdapter({ OutfitAskViewHolder(this@AskForOutfitFragment, it) }, R.layout.outfit_ask_item)
        binding.recyclerView.adapter = outfitAsksAdapter

        outfitAsksViewModel.getOutfitAsksFromFirestoreToRecyclerView(outfitAsksAdapter)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.search.setOnQueryTextListener(this)

        binding.addItemButton.setOnClickListener{
            findNavController().navigate(R.id.action_AskForOutfitFragment_to_AddOutfitAskFragment)
        }

    }

    fun viewSingleOutfitAsk(outfitAsk: OutfitAsk){
        val bundle = Bundle()
        bundle.putParcelable("outfitAsk", Parcels.wrap(outfitAsk));
        findNavController().navigate(R.id.action_AskForOutfitFragment_to_SingleOutfitAskFragment, bundle)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        outfitAsksAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        outfitAsksAdapter.filter.filter(newText)
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}