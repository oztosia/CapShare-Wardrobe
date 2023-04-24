package com.example.mypersonalwardrobe.domain

import GenericAdapter
import android.content.ContentValues
import android.util.Log
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.utils.ItemsListHolder.ItemsListHolder.list
import com.example.mypersonalwardrobe.utils.ObservedPostsSetHolder.ObservedPostsSetHolder.postSet
import com.example.mypersonalwardrobe.models.Post
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebasePostsRepo: FirebaseGenericRepo() {

    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection(FirebaseConst.POST_HASHTAGS)


    private suspend fun getPreferences(path: String, documentPath: String, stringToRead: String): String { return db.collection(path).document(documentPath).get().await().getString(stringToRead).toString() }

    private fun stringToList(string: String): List<String> { return string.trim().splitToSequence(' ').filter { it.isNotEmpty() }.toList() }

    private suspend fun getObservedHashtags(): List<String> { return stringToList(getPreferences(FirebaseConst.HASHTAGS_PREFERENCES, FirebaseConst.OBSERVED_HASHTAGS, "observed")) }

    private suspend fun getBlockedHashtags(): List<String> { return stringToList(getPreferences(FirebaseConst.HASHTAGS_PREFERENCES, FirebaseConst.BLOCKED_HASHTAGS, "blocked")) }


    suspend fun createListOfPostsToDisplay() {
        for (observe in getObservedHashtags()) {
            collectionRef.document(observe).collection("/postId")
                .get().addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (document in documents)
                            postSet.add(document.id)
                    } else {
                    }
                }
                .addOnFailureListener { exception -> }
        }
    }

    suspend fun loadData(adapter: GenericAdapter<Post>, lastDocumentSnapshotMutableLiveData: MutableLiveData<DocumentSnapshot>) {

        val blockedList = getBlockedHashtags()
        var query: Query

        //adapter.dataSet.clear()

        Log.w(ContentValues.TAG, "lastdoc: " + lastDocumentSnapshotMutableLiveData.value.toString())
        if (lastDocumentSnapshotMutableLiveData.value == null) {
            query = FirebaseFirestore.getInstance().collection(FirebaseConst.POSTS)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(50)
        } else {
            query = FirebaseFirestore.getInstance().collection(FirebaseConst.POSTS)
                .orderBy("date", Query.Direction.DESCENDING)
                .startAfter(lastDocumentSnapshotMutableLiveData.value as DocumentSnapshot)
                .limit(50)
        }

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        //adapter.dataSet.clear()
                        for (document in documents) {
                            if (postSet.contains(document.id)) {
                                var entity = document.toObject(Post::class.java)
                                val entityHashtags = stringToList(entity!!.hashtags)
                                if (!entityHashtags.stream().anyMatch(blockedList::contains))
                                    adapter.dataSet.add(entity!!)
                                adapter.notifyDataSetChanged()
                            }
                            lastDocumentSnapshotMutableLiveData.postValue(snapshot.documents[snapshot.size() - 1])

                        }

                    }
                }
            }
        }
    }

    fun addPost(text: String, hashtags: String, progressBar: ProgressBar) {

        val timestamp = System.currentTimeMillis().toString()
        val id = UUID.randomUUID().toString()
        val path = FirebaseConst.POSTS + id

        val postMap = hashMapOf<String, Any>(
            "id" to id,
            "date" to timestamp,
            "hashtags" to hashtags,
            "text" to text,
            "authorUid" to FirebaseConst.CURRENT_USER
        )

        val hashtagsList = stringToList(hashtags)

        for (hashtag in hashtagsList) {
            val postIdMap = hashMapOf<String, Any>("date" to timestamp)
            set(FirebaseConst.POST_HASHTAGS + "/" + hashtag + "/postId", id, postIdMap)
        }
        set(FirebaseConst.POSTS, id, postMap)
        uploadMultipleImages(list, path, progressBar)
    }


    fun uploadMultipleImages(
        urisList: MutableList<String>,
        path: String,
        progressBar: ProgressBar
    ) {
        for (uri in urisList) {
            addImage(
                uri.toUri(), "", FirebaseConst.STORAGE_POSTS_IMAGES,
                path + "/images", progressBar
            )
        }
        urisList.clear()
    }

    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>) {
        adapter.dataSet.addAll(list)
        adapter.notifyDataSetChanged()
    }

}

