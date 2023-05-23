package com.oztosia.capsharewardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.domain.FirebaseChatRepo
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder
import com.oztosia.capsharewardrobe.models.Message
import com.google.firebase.firestore.Query
import com.oztosia.capsharewardrobe.adapters.ChatAdapter

class ChatViewModel: ViewModel() {

    val chatRepo: FirebaseChatRepo = FirebaseChatRepo()


    fun getMessagesFromFirestoreToRecyclerView(
        adapter: ChatAdapter,
        chatId: String
    ) {
        chatRepo.getDataToRecyclerView(adapter, "date",
            Query.Direction.DESCENDING,
            FirebaseConst.CHAT + "$chatId/messages")
    }

    fun send(text: String, chatId: String){ chatRepo.send(text, chatId) }

    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>){
        adapter.dataSet.addAll(ItemsListHolder.list)
        adapter.notifyDataSetChanged()
    }

    fun deleteImage(uri: String){ ItemsListHolder.list.remove(uri) }

}