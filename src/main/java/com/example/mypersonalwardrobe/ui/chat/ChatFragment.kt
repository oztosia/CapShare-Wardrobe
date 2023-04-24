package com.example.mypersonalwardrobe.ui.chat

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
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.adapters.viewholders.ItemToPostOrAskViewHolder
import com.example.mypersonalwardrobe.adapters.viewholders.MessageViewHolder
import com.example.mypersonalwardrobe.databinding.FragmentChatBinding
import com.example.mypersonalwardrobe.utils.ItemsListHolder.ItemsListHolder.list
import com.example.mypersonalwardrobe.models.Message
import com.example.mypersonalwardrobe.models.User
import com.example.mypersonalwardrobe.viewmodels.ChatViewModel
import org.parceler.Parcels

class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var chatId: String
    lateinit var messageAdapter: GenericAdapter<Message>
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
        messageAdapter = GenericAdapter({ MessageViewHolder(it) }, R.layout.send_message_item)
        binding.recyclerView.adapter = messageAdapter
        binding.recyclerView.isNestedScrollingEnabled = false

        val imageLayoutManager = GridLayoutManager(MyPersonalWardrobe.getAppContext(), 3)
        binding.imageRecyclerView.layoutManager = imageLayoutManager
        imageAdapter = GenericAdapter({ ItemToPostOrAskViewHolder(it) },
            R.layout.photo_item)
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
            .placeholder(R.drawable.ic_launcher_foreground)
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


}