package com.example.mypersonalwardrobe.firebase

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.adapters.GalleryRecyclerAdapter
import com.example.mypersonalwardrobe.adapters.PostImageRecyclerAdapter
import com.example.mypersonalwardrobe.models.Photo
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class FirebasePhotosRepo {

    val application = MyPersonalWardrobe.getAppContext()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()
        .reference




    fun uploadImage(uri: Uri, galleryTypePath: String, progressBar: ProgressBar, hashtags: String) {

        val imageName: String
        val path: DocumentReference

        if (galleryTypePath == "profileImage") {
            imageName = "profileImage"
            path =  FirebaseFirestore.getInstance().collection("users")
                .document(firebaseAuth.currentUser!!.uid)
        }
        else {
            imageName = UUID.randomUUID().toString()
            path =  FirebaseFirestore.getInstance().collection("users")
                .document(firebaseAuth.currentUser!!.uid).collection(galleryTypePath)
                .document(imageName)
        }

        progressBar.visibility = View.VISIBLE

        val imagesReference = firebaseStorage.child("/" + firebaseAuth.currentUser?.uid.toString()
                + "/"
                + galleryTypePath).child(imageName)
        imagesReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
            imagesReference.downloadUrl.addOnSuccessListener { uri ->

                val downloadUrl = uri.toString()
                val action: Task<Void>

                if (galleryTypePath != "profileImage") {
                    action = path.set(mapOf(
                    "downloadURL" to downloadUrl,
                    "hashtags" to hashtags
                    ))
                }
                else
                    action = path.update(mapOf(
                        "profileImage" to downloadUrl,
                    ))

                   action
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
            }
            progressBar.visibility = View.GONE
        }
    }

    fun getDataFromFirestoreToRecyclerView(galleryTypePath: String,
                                           userImageFromFirebase: ArrayList<Photo>,
                                           adapter: GalleryRecyclerAdapter) {

        FirebaseFirestore.getInstance().collection("users")
            .document(firebaseAuth.currentUser!!.uid).collection(galleryTypePath)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(
                        MyPersonalWardrobe.getAppContext(),
                        exception.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {

                            userImageFromFirebase.clear()

                            val documents = snapshot.documents
                            for (document in documents) {

                                val downloadURL = document.get("downloadURL") as String
                                val hashtags = document.get("hashtags") as String
                                val docId = document.id

                                val photo = Photo(downloadURL, hashtags, docId)

                                userImageFromFirebase.add(photo)
                                adapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }}}


    fun deleteImage(photo: Photo,
                    galleryTypePath: String,
                    adapter: GalleryRecyclerAdapter){

        val imagesReference = firebaseStorage.child("/"
                + firebaseAuth.currentUser?.uid.toString()
                + "/"
                + galleryTypePath).child(photo.docId)

        imagesReference.delete()
            .addOnSuccessListener(OnSuccessListener<Void?> {

                FirebaseFirestore.getInstance().collection("users")
                    .document(firebaseAuth.currentUser!!.uid).collection(galleryTypePath)
                    .document(photo.docId).delete()
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Deleted document with ID ${it}")
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error delete document $exception")
                    }
            }).addOnFailureListener(OnFailureListener {

            })
            adapter.notifyDataSetChanged()
        }


}

