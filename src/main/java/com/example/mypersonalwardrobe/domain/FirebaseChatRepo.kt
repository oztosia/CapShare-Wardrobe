package com.example.mypersonalwardrobe.domain

import androidx.core.net.toUri
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.utils.ItemsListHolder.ItemsListHolder.list
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import java.util.*

class FirebaseChatRepo: FirebaseGenericRepo() {


    fun send(text: String, chatId: String){

        val id = UUID.randomUUID().toString()

        val path = FirebaseConst.CHAT + chatId +"/"

        val date = System.currentTimeMillis().toString()

        val postMap = hashMapOf<String, Any>(
            "authorUid" to FirebaseConst.CURRENT_USER,
            "date" to date,
            "text" to text,
            "downloadUrl" to "",
        )
        set(path + "messages/", id, postMap)
        uploadImage(list, path + "messages/", id)
        list.clear()
    }

    fun uploadImage(
        urisList: MutableList<String>,
        path: String,
        documentPath: String,
    ) {
        for (uri in urisList)
        replaceImage(uri.toUri(),
                "downloadUrl",
                FirebaseConst.STORAGE_CHAT_IMAGES,
                path,
                documentPath,
        null)

    }

}