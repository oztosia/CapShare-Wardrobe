package com.oztosia.capsharewardrobe.ui.usersList

import GenericAdapter
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.databinding.FragmentBaseRecyclerviewWithSearchBinding
import com.oztosia.capsharewardrobe.models.User
import com.oztosia.capsharewardrobe.adapters.viewholders.UsersListViewHolder
import com.oztosia.capsharewardrobe.viewmodels.UsersListViewModel
import org.parceler.Parcels

class UsersListFragment: Fragment(), SearchView.OnQueryTextListener{

    private var _binding: FragmentBaseRecyclerviewWithSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var usersListViewModel: UsersListViewModel
    lateinit var adapter: GenericAdapter<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usersListViewModel = ViewModelProvider(requireActivity()).get(UsersListViewModel::class.java)
        _binding = FragmentBaseRecyclerviewWithSearchBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(CapShareWardrobe.getAppContext(), 4)
        binding.recyclerView.layoutManager = layoutManager
        adapter = GenericAdapter({ UsersListViewHolder(it, this@UsersListFragment) }, R.layout.item_user)
        binding.recyclerView.adapter = adapter

        binding.search.isIconified

        val bundle = arguments?.getString("home")
        Log.d(ContentValues.TAG, "arguments receive: " + bundle.toString())
        if (bundle == "home") {
            getObservedUsers()

            arguments?.remove("home")
        }
        else {
            getAllUsers()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.search.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.search.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }

    fun getAllUsers(){
        binding.addItemButton.setImageResource(R.drawable.ic_baseline_arrow_back_24)
        adapter.dataSet.clear()
        usersListViewModel.getUsersDataFromFirestoreToRecyclerView(adapter)
        binding.addItemButton.setOnClickListener { getObservedUsers() }
    }

    fun getObservedUsers(){
        binding.addItemButton.setImageResource(R.drawable.ic_baseline_add_24)
        adapter.dataSet.clear()
        usersListViewModel.getObservedUsersDataFromFirestoreToRecyclerView(adapter)
        binding.addItemButton.setOnClickListener { getAllUsers() }
    }

    fun viewProfile(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user", Parcels.wrap(user));
        Log.d(ContentValues.TAG, "arguments: " + bundle.toString())
        findNavController().navigate(R.id.action_UsersListFragment_to_ViewProfileFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
