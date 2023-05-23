package com.oztosia.capsharewardrobe.ui.preferences

import GenericAdapter
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oztosia.capsharewardrobe.R
import androidx.lifecycle.ViewModelProvider
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.adapters.viewholders.HashtagListViewHolder
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.databinding.FragmentPreferencesBinding
import com.oztosia.capsharewardrobe.viewmodels.PreferencesViewModel
import com.google.android.flexbox.*


class PreferencesFragment : Fragment() {

    private var _binding: FragmentPreferencesBinding? = null
    private val binding get() = _binding!!
    val application = CapShareWardrobe.getAppContext()

    lateinit var observedHashtagsAdapter: GenericAdapter<String>
    lateinit var blockedHashtagsAdapter: GenericAdapter<String>

    private lateinit var preferencesViewModel: PreferencesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        preferencesViewModel = ViewModelProvider(requireActivity())[PreferencesViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preferences, container, false)

        val hashtagsLayoutManager = FlexboxLayoutManager(application)
        hashtagsLayoutManager.flexDirection = FlexDirection.ROW
        hashtagsLayoutManager.flexWrap = FlexWrap.WRAP
        hashtagsLayoutManager.justifyContent = JustifyContent.CENTER
        hashtagsLayoutManager.alignItems = AlignItems.STRETCH

        binding.observedHashtagsRecyclerView.layoutManager = hashtagsLayoutManager
        observedHashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) },
            R.layout.item_hashtag)
        binding.observedHashtagsRecyclerView.adapter = observedHashtagsAdapter
        preferencesViewModel
            .getHashtagsDataFromFirestoreToRecyclerView(observedHashtagsAdapter,
                FirebaseConst.OBSERVED_HASHTAGS,
                "observed"
                )

        val blockedHashtagsLayoutManager = FlexboxLayoutManager(application)
        blockedHashtagsLayoutManager.flexDirection = FlexDirection.ROW
        blockedHashtagsLayoutManager.flexWrap = FlexWrap.WRAP
        blockedHashtagsLayoutManager.justifyContent = JustifyContent.CENTER
        blockedHashtagsLayoutManager.alignItems = AlignItems.STRETCH

        binding.blockedHashtagsRecyclerView.layoutManager = blockedHashtagsLayoutManager
        blockedHashtagsAdapter = GenericAdapter({ HashtagListViewHolder(it) },
            R.layout.item_hashtag)
        binding.blockedHashtagsRecyclerView.adapter = blockedHashtagsAdapter

        preferencesViewModel
            .getHashtagsDataFromFirestoreToRecyclerView(blockedHashtagsAdapter,
                FirebaseConst.BLOCKED_HASHTAGS,
                "blocked"
            )

        preferencesViewModel.getObservedHashtagsMutableLiveData().observe(viewLifecycleOwner) { observed ->
            val editable: Editable = SpannableStringBuilder(observed)
            binding.observedHashtagsEditText.text = editable
        }

        preferencesViewModel.getBlockedHashtagsMutableLiveData().observe(viewLifecycleOwner) { blocked ->
            val editable: Editable = SpannableStringBuilder(blocked)
            binding.blockedHashtagsEditText.text = editable
        }

        //binding.lifecycleOwner = this
        binding.viewModel = preferencesViewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editModeButton.setOnClickListener {
            binding.blockedHashtagsRecyclerView.visibility = View.GONE
            binding.observedHashtagsRecyclerView.visibility = View.GONE
            binding.editModeButton.visibility = View.GONE
            binding.blockedHashtagsEditText.visibility = View.VISIBLE
            binding.observedHashtagsEditText.visibility = View.VISIBLE
            binding.confirmButton.visibility = View.VISIBLE
        }

        binding.confirmButton.setOnClickListener {


            val observedHashtags = binding.observedHashtagsEditText.text
            val blockedHashtags = binding.blockedHashtagsEditText.text

            preferencesViewModel.updateObservedHashtags(observedHashtags)
            preferencesViewModel.updateBlockedHashtags(blockedHashtags)

            preferencesViewModel.getHashtagsDataFromFirestoreToRecyclerView(observedHashtagsAdapter, FirebaseConst.OBSERVED_HASHTAGS, "observed")
            preferencesViewModel.getHashtagsDataFromFirestoreToRecyclerView(blockedHashtagsAdapter, FirebaseConst.BLOCKED_HASHTAGS, "blocked")


            binding.blockedHashtagsRecyclerView.visibility = View.VISIBLE
            binding.observedHashtagsRecyclerView.visibility = View.VISIBLE
            binding.editModeButton.visibility = View.VISIBLE
            binding.blockedHashtagsEditText.visibility = View.GONE
            binding.observedHashtagsEditText.visibility = View.GONE
            binding.confirmButton.visibility = View.GONE
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}