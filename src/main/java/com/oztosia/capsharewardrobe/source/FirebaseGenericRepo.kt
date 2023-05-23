package com.oztosia.capsharewardrobe.source

import GenericAdapter
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.models.Photo
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.*

open class FirebaseGenericRepo {

    val firestore = FirebaseFirestore.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance().reference
    val application = CapShareWardrobe.getAppContext()

    fun add(path: String, data: Map<String, Any>) {
        firestore.collection(path)
            .add(data)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun set(collectionPath: String, documentPath: String, data: Map<String, Any>) {
        firestore.collection(collectionPath)
            .document(documentPath)
            .set(data)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun get(path: String,
            documentPath: String,
            value: String,
            mutableLiveData: MutableLiveData<String>): MutableLiveData<String>{

        val documentReference: DocumentReference = firestore.collection(path)
            .document(documentPath)

            documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
            mutableLiveData
                .postValue(documentSnapshot.getString(value))
                Log.d(TAG, "docSnapshot: " + documentSnapshot)
                if (!documentSnapshot.exists())
                    mutableLiveData.postValue(" ")
            }
        return mutableLiveData
    }

  suspend fun getWithCoroutines(path: String, documentPath: String, value: String): String {
      return firestore.collection(path).document(documentPath).get().await().getString(value).toString()}



    @SuppressLint("NotifyDataSetChanged")
    inline fun <reified T> getDataToRecyclerView(adapter: GenericAdapter<T>,
                                                  path: String) {
        firestore.collection(path)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, "Error getting documents: ", exception)
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            adapter.dataSet.clear()
                            val documents = snapshot.documents
                            for (document in documents) {
                                val entity = document.toObject<T>()!!
                                adapter.dataSet.add(entity!!)
                                adapter.notifyDataSetChanged()
                                Log.d(TAG, "data from firebase test" + adapter.dataSet)
                            }
                        }}}}
            }


    @SuppressLint("NotifyDataSetChanged")
    fun getDataToList(dataList: ArrayList<String>,
                                                 path: String) {
        firestore.collection(path)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, "Error getting documents: ", exception)
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            dataList.clear()
                            val documents = snapshot.documents
                            for (document in documents) {
                                dataList.add(document.id)
                                Log.d(TAG, "data from firebase test" + dataList.toString())
                            }
                        }}}}
    }



    @SuppressLint("NotifyDataSetChanged")
    inline fun <reified T> getDataToRecyclerViewWithOrder(adapter: GenericAdapter<T>,
                                                          field: String,
                                                          direction: Query.Direction,
                                                 path: String) {
        firestore.collection(path).orderBy(field, direction)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, "Error getting documents: ", exception)
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            adapter.dataSet.clear()
                            val documents = snapshot.documents
                            for (document in documents) {
                                val entity = document.toObject<T>()!!
                                adapter.dataSet.add(entity!!)
                                adapter.notifyDataSetChanged()
                            }
                        }}}}
    }


    @SuppressLint("NotifyDataSetChanged")
    inline fun <reified T> getDataToRecyclerViewWithCondition(adapter: GenericAdapter<T>,
                                                              field: String,
                                                              value: String,
                                                 path: String) {


        firestore.collection(path).whereEqualTo(field, value)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, "Error getting documents: ", exception)
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            val documents = snapshot.documents
                            adapter.dataSet.clear()
                            for (document in documents) {
                                val entity = document.toObject<T>()!!
                                adapter.dataSet.add(entity!!)
                                adapter.notifyDataSetChanged()
                                Log.d(TAG, "data from firebase test" + adapter.dataSet)
                            }
                        }}}}
    }




    @SuppressLint("NotifyDataSetChanged")
    inline fun <reified T> getDataToRecyclerViewWithQuery(
        adapter: GenericAdapter<T>,
        path: String,
        queryPath: String
    ) {

        firestore.collection(queryPath).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list: MutableList<String> = ArrayList()
                for (document in task.result) {
                    list.add(document.id)
                }
                firestore.collection(path)
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.w(TAG, "Error getting documents: ", exception)
                        } else {
                            if (snapshot != null) {
                                if (!snapshot.isEmpty) {
                                    val documents = snapshot.documents
                                    adapter.dataSet.clear()
                                    for (document in documents) {
                                        if (list.contains(document.id)) {
                                            var entity = document.toObject(T::class.java)
                                            adapter.dataSet.add(entity!!)
                                            adapter.notifyDataSetChanged()
                                        }}}}}}}
            else {
                Log.d(TAG, "Error getting documents: ", task.exception)
            }
        }
    }

    fun getFromDocumentAndSplit(path: String, documentPath: String, stringToRead: String, adapter: GenericAdapter<String>, ){
        val documentReference = firestore.collection(path).document(documentPath)
        adapter.dataSet.clear()
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            val str: String = documentSnapshot.getString(stringToRead).toString()
            val strList = str.trim().splitToSequence(' ')
                .filter { it.isNotEmpty() }
                .toList()
            adapter.dataSet.addAll(strList)
            adapter.notifyDataSetChanged()
        }
    }


    fun update(collectionPath: String, documentPath: String, data: Map<String, String>){
        firestore.collection(collectionPath)
            .document(documentPath)
            .update(data)
            .addOnCompleteListener {
                Log.w(TAG, "data updated successfully")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

    fun delete(collectionPath: String, documentPath: String) {
        firestore.collection(collectionPath)
            .document(documentPath)
            .delete()
            .addOnSuccessListener {}
            .addOnFailureListener { exception -> Log.w(TAG, "Error getting documents: ", exception) }
    }

   suspend fun deleteFromStorage(storageDocId: String, storagePath: String) {
       try { runBlocking { firebaseStorage.child(storagePath).child(storageDocId).delete().await() }
       }
       catch (e:Exception){
           Log.w(TAG, "Error getting documents: ", e)
       }
    }

    fun replaceImage(uri: Uri, imageName: String, storagePath: String, firestorePath: String, documentPath: String, progressBar: ProgressBar?){

        progressBar?.visibility = View.VISIBLE

        val storageReference = firebaseStorage.child(storagePath)
            .child(imageName)

        storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        firestore.collection(firestorePath).document(documentPath)
                            .update(mapOf(imageName to downloadUrl)).addOnCompleteListener { task ->
                                if (task.isComplete && task.isSuccessful) { } }
                            .addOnFailureListener { exception -> }
                        progressBar?.visibility = View.GONE
                    }}}


    fun addImage(uri: Uri, hashtags: String, storagePath: String, firestorePath: String, progressBar: ProgressBar) {

            val imageName = UUID.randomUUID().toString()
            progressBar.visibility = View.VISIBLE
            val storageReference = firebaseStorage.child(storagePath).child(imageName)

        storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    firestore.collection(firestorePath).document(imageName)
                        .set(mapOf("downloadURL" to downloadUrl, "docId" to imageName, "hashtags" to hashtags))
                        .addOnCompleteListener { task ->
                            if (task.isComplete && task.isSuccessful) {
                            } }.addOnFailureListener { exception -> }
                    progressBar.visibility = View.GONE
                }}}

    fun addImage(uri: Uri, data: MutableMap<String, Any>, storagePath: String, firestorePath: String, progressBar: ProgressBar) {

        val imageName = UUID.randomUUID().toString()
        progressBar.visibility = View.VISIBLE
        val storageReference = firebaseStorage.child(storagePath).child(imageName)

        storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                data.put("downloadURL", downloadUrl)
                data.put("docId", imageName)
                firestore.collection(firestorePath).document(imageName)
                    .set(data)
                    .addOnCompleteListener { task ->
                        if (task.isComplete && task.isSuccessful) {
                        } }.addOnFailureListener { exception -> }
                progressBar.visibility = View.GONE
            }}}

    inline fun <reified T> deleteImageFromFirestore(photo: Photo, storagePath: String, firestorePath: String, adapter: GenericAdapter<T>){

        val imagesReference = firebaseStorage.child(storagePath).child(photo.docId)

        imagesReference.delete()
            .addOnSuccessListener(OnSuccessListener<Void?> {

                firestore.collection(firestorePath)
                    .document(photo.docId).delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Deleted document with ID ${it}")
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error delete document $exception")
                    }
            }).addOnFailureListener(OnFailureListener {

            })
        adapter.notifyDataSetChanged()
    }



}
