package com.oztosia.capsharewardrobe.ui.chat

import GenericAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.ChatAdapter
import com.oztosia.capsharewardrobe.adapters.viewholders.ChatImageViewHolder
import com.oztosia.capsharewardrobe.databinding.FragmentChatBinding
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder.Singleton.list
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.models.User
import com.oztosia.capsharewardrobe.viewmodels.ChatViewModel
import org.parceler.Parcels

class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var chatId: String
    lateinit var messageAdapter: ChatAdapter
    lateinit var imageAdapter: GenericAdapter<String>
    private lateinit var chatViewModel: ChatViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)

        user = Parcels.unwrap(arguments?.getParcelable("user"))
        chatId = arguments?.getString("chat").toString()

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,true)


        binding.recyclerView.layoutManager = layoutManager
        messageAdapter = ChatAdapter()
        binding.recyclerView.adapter = messageAdapter
        binding.recyclerView.isNestedScrollingEnabled = false

        val imageLayoutManager = GridLayoutManager(CapShareWardrobe.getAppContext(), 3)
        binding.imageRecyclerView.layoutManager = imageLayoutManager
        imageAdapter = GenericAdapter({ ChatImageViewHolder(this@ChatFragment, it) },
            R.layout.item_image)
        binding.imageRecyclerView.adapter = imageAdapter

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.getMessagesFromFirestoreToRecyclerView(messageAdapter, chatId)

        chatViewModel.getDataFromPhotoListToRecyclerView(imageAdapter)


        binding.userName.text = user.userName

        Glide.with(this).load(user.profileImage)
            .centerCrop()
            .placeholder(R.drawable.icon)
            .into(binding.miniProfilePhotoImageView)


        binding.sendIcon.setOnClickListener {
            val text: String = binding.messageText.text.toString()
            if (text.isNotEmpty())
            chatViewModel.send(text, chatId)
            binding.messageText.text.clear()
            list.clear()
            imageAdapter.dataSet.clear()
            imageAdapter.notifyDataSetChanged()

        }

        binding.addImageIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("intent", "chat")
            findNavController().navigate(R.id.action_ChatFragment_to_photoBottomSheet, bundle)
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun delete(uri: String) {
        chatViewModel.deleteImage(uri)
        imageAdapter.dataSet.remove(uri)
        imageAdapter.notifyDataSetChanged()
    }
}