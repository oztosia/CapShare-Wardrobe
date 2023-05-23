package com.oztosia.capsharewardrobe.domain

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.oztosia.capsharewardrobe.adapters.ChatAdapter
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.constants.MessagesTypesConst
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder.Singleton.list
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import java.util.*

class FirebaseChatRepo: FirebaseGenericRepo() {


    fun send(text: String, chatId: String){

        val id = UUID.randomUUID().toString()
        val path = FirebaseConst.CHAT + chatId +"/"
        val date = System.currentTimeMillis().toString()
        val postMap = hashMapOf<String, Any>("authorUid" to FirebaseConst.CURRENT_USER, "date" to date, "text" to text, "downloadUrl" to "",)
        set(path + "messages/", id, postMap)
        uploadImage(list, path + "messages/", id)
        list.clear()
    }

    fun uploadImage(urisList: MutableList<String>, path: String, documentPath: String, ) {
        for (uri in urisList) replaceImage(uri.toUri(), "downloadUrl", FirebaseConst.STORAGE_CHAT_IMAGES, path, documentPath, null) }

    @SuppressLint("NotifyDataSetChanged")
    fun getDataToRecyclerView(adapter: ChatAdapter, field: String, direction: Query.Direction, path: String) {
        firestore.collection(path).orderBy(field, direction)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) { }
                else { if (snapshot != null) { if (!snapshot.isEmpty) {
                            adapter.dataSet.clear()
                            val documents = snapshot.documents
                            for (document in documents) {
                                val entity = document.toObject<Message>()!!
                                adapter.dataSet.add(entity)
                                adapter.notifyDataSetChanged()
                            } }}}}
    }

}