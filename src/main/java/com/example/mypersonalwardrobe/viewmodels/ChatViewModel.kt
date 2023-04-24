package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import androidx.lifecycle.ViewModel
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.domain.FirebaseChatRepo
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.utils.ItemsListHolder
import com.example.mypersonalwardrobe.models.Message
import com.google.firebase.firestore.Query

class ChatViewModel: ViewModel() {

    val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    val chatRepo: FirebaseChatRepo = FirebaseChatRepo()


    fun getMessagesFromFirestoreToRecyclerView(adapter: GenericAdapter<Message>,
                                                      chatId: String
    ) {
        firebaseRepo.getDataToRecyclerViewWithOrder(adapter, "date",
            Query.Direction.DESCENDING,
            FirebaseConst.CHAT + "$chatId/messages")
    }

    fun send(text: String, chatId: String){
        chatRepo.send(text, chatId)
    }


    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>
    ){
        adapter.dataSet.addAll(ItemsListHolder.list)
        adapter.notifyDataSetChanged()
    }


}