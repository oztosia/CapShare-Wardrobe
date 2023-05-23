package com.oztosia.capsharewardrobe.ui.outfits

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
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.viewholders.OutfitAskViewHolder
import com.oztosia.capsharewardrobe.databinding.FragmentOutfitsBinding
import com.oztosia.capsharewardrobe.models.OutfitAsk
import com.oztosia.capsharewardrobe.viewmodels.OutfitAskViewModel
import org.parceler.Parcels


class OutfitsFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var outfitAsksViewModel: OutfitAskViewModel
    private var _binding: FragmentOutfitsBinding? = null
    private val binding get() = _binding!!

    lateinit var outfitAsksAdapter: GenericAdapter<OutfitAsk>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        outfitAsksViewModel = ViewModelProvider(requireActivity())[OutfitAskViewModel::class.java]

        _binding = FragmentOutfitsBinding.inflate(inflater, container, false)

        outfitAsksViewModel._lastDocumentSnapshotMutableLiveData.value = null

        val postLayoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)

        binding.recyclerView.layoutManager = postLayoutManager
        outfitAsksAdapter = GenericAdapter({ OutfitAskViewHolder(this@OutfitsFragment, it) }, R.layout.item_outfit_ask)
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

        binding.bottomNavigationView.setOnNavigationItemSelectedListener setNavigationItemSelectedListener@{
            when (it.itemId) {
                R.id.nav_all -> {
                    outfitAsksAdapter.dataSet.clear()
                    outfitAsksViewModel.getOutfitAsksFromFirestoreToRecyclerView(outfitAsksAdapter)
                }
                R.id.nav_my -> {
                    outfitAsksAdapter.dataSet.clear()
                    outfitAsksViewModel.getMyOutfitAsksFromFirestoreToRecyclerView(outfitAsksAdapter)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun viewOutfitAsk(outfitAsk: OutfitAsk){
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