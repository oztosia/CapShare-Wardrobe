package com.example.mypersonalwardrobe.domain

import GenericAdapter
import android.content.ContentValues
import android.util.Log
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.utils.ItemsListHolder.ItemsListHolder.list
import com.example.mypersonalwardrobe.models.OutfitAsk
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*


class FirebaseOutfitAsksRepo(): FirebaseGenericRepo() {

    fun addOutfitAsk(title: String, text: String, hashtags: String){

        val id = UUID.randomUUID().toString()

        val path = FirebaseConst.OUTFIT_ASKS + id

        val timestamp = System.currentTimeMillis().toString()

        val postMap = hashMapOf<String, Any>(
            "id" to id,
            "date" to timestamp,
            "hashtags" to hashtags,
            "text" to text,
            "title" to title,
            "authorUid" to  FirebaseConst.CURRENT_USER.toString(),
        )
        set(FirebaseConst.OUTFIT_ASKS, id, postMap)
        uploadMultipleDocuments(list, path)
    }


    fun uploadMultipleDocuments(urisList: List<String>, path: String) {
        for (uri in urisList) {
            val postMap = hashMapOf<String, Any>(
                "downloadURL" to uri,
            )
            add(path + "/images", postMap)
        }
    }


    fun getOutfitsAsksToRecyclerView(adapter: GenericAdapter<OutfitAsk>){

        val monthAgoInMillis = System.currentTimeMillis().minus(2629800000).toString()

        FirebaseFirestore.getInstance().collection(FirebaseConst.OUTFIT_ASKS)
            .orderBy("date", Query.Direction.DESCENDING)
            .whereGreaterThan("date", monthAgoInMillis)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            adapter.dataSet.clear()
                            val documents = snapshot.documents
                            for (document in documents) {
                                val entity = document.toObject<OutfitAsk>()!!
                                adapter.dataSet.add(entity!!)
                                adapter.notifyDataSetChanged()
                            }
                        }}}}

    }



}