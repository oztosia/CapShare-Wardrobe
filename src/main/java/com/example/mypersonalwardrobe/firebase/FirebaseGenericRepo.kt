package com.example.mypersonalwardrobe.firebase

import GenericAdapter
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.constants.FirebasePathsConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


open class FirebaseGenericRepo {

    val firestore = FirebaseFirestore.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()
    val application = MyPersonalWardrobe.getAppContext()

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

    @SuppressLint("NotifyDataSetChanged")
    inline fun <reified T> getDataToRecyclerView(dataList: ArrayList<T>,
                                                 adapter: GenericAdapter<T>,
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

                                var entity = document.toObject(T::class.java)

                                dataList.add(entity!!)
                                adapter.notifyDataSetChanged()

                                Log.w(TAG, "data from firebase " + dataList.size)
                                }}}}}}


    @SuppressLint("NotifyDataSetChanged")
    inline fun <reified T> getDataToRecyclerViewWithQuery(
        dataList: ArrayList<T>,
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
                                    dataList.clear()
                                    val documents = snapshot.documents
                                    for (document in documents) {
                                        if (list.contains(document.id)) {
                                            var entity = document.toObject(T::class.java)
                                            dataList.add(entity!!)
                                            adapter.notifyDataSetChanged()
                                        }}}}}}}
            else {
                Log.d(TAG, "Error getting documents: ", task.exception)
            }
        }
    }
    fun update(collectionPath: String, documentPath: String, data: Map<String, String>){
        firestore.collection(collectionPath)
            .document(documentPath)
            .update(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

    fun delete(){}


    fun addImage(uri: Uri,
                 storagePath: String,
                 firestorePath: String,
                 progressBar: ProgressBar) {

            val imageName = UUID.randomUUID().toString()
            progressBar.visibility = View.VISIBLE

            val storageReference = firebaseStorage.reference.child(storagePath)
                .child(imageName)

        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->

                    val downloadUrl = uri.toString()

                    firestore.collection(firestorePath)
                        .add(mapOf(
                            "downloadUrl" to downloadUrl
                        ))
                        .addOnCompleteListener { task ->
                            if (task.isComplete && task.isSuccessful) {
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(
                                application,
                                exception.localizedMessage.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    progressBar.visibility = View.GONE
                }
            }
    }

    fun deleteImageFromFirestore(){

    }

}
