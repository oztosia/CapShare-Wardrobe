package com.example.mypersonalwardrobe.viewmodels

import GenericAdapter
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.source.FirebaseGenericRepo
import com.example.mypersonalwardrobe.models.Photo
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {

    private val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    val _userNameMutableLiveData = MutableLiveData<String>()


    fun getProfileImage(uid: String, profileImageView: ImageView, context: Context){
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            Glide
                .with(context)
                .load(documentSnapshot.getString("profileImage"))
                .centerCrop()
                .into(profileImageView)
        }
    }

    fun getUserName(uid: String, userNameTextView: TextView){
        val df: DocumentReference = FirebaseFirestore.getInstance().collection("users")
            .document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            userNameTextView.text = documentSnapshot.getString("userName")
        }
    }
    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>, postId: String) { firebaseRepo.getFromDocumentAndSplit(FirebaseConst.POSTS, postId, "hashtags", adapter) }

    fun checkIfPostIsLiked(postId: String, likeIcon: ImageView, unlikeIcon: ImageView){
        FirebaseFirestore.getInstance()
       .collection(FirebaseConst.POSTS + "$postId/likes")
            .document(FirebaseConst.CURRENT_USER)
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                likeIcon.visibility = View.GONE
                    unlikeIcon.visibility = View.VISIBLE
            } else {
                    likeIcon.visibility = View.VISIBLE
                    unlikeIcon.visibility = View.GONE
            }
        }
    }

    fun getPostsImagesFromFirestoreToRecyclerView(adapter: GenericAdapter<Photo>, postId: String) { firebaseRepo.getDataToRecyclerView(adapter, FirebaseConst.POSTS + "$postId/images") }

    fun getLikesFromFirestore(postId: String, likesTextView: TextView){
        val query = FirebaseFirestore.getInstance()
            .collection(FirebaseConst.POSTS + "$postId/likes")
        val countQuery = query.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                likesTextView.text = snapshot.count.toString()
                Log.d(ContentValues.TAG, "Count:" + snapshot.count.toString())
            } else {
                Log.d(ContentValues.TAG, "Count failed: ", task.exception)
            }
        }
    }


    fun like(postId: String, likesTextView: TextView, likeIcon: ImageView, unlikeIcon: ImageView) {

        val postMap = hashMapOf<String, Any>(
            "uid" to FirebaseConst.CURRENT_USER,
            "postId" to postId)

        FirebaseFirestore.getInstance().collection(FirebaseConst.POSTS + "$postId/likes")
            .document(FirebaseConst.CURRENT_USER)
            .set(postMap)
            .addOnSuccessListener {
                getLikesFromFirestore(postId, likesTextView)
                checkIfPostIsLiked(postId, likeIcon, unlikeIcon)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    fun unlike(postId: String, likesTextView: TextView, likeIcon: ImageView, unlikeIcon: ImageView){
        FirebaseFirestore.getInstance().collection(FirebaseConst.POSTS + "$postId/likes")
            .document(FirebaseConst.CURRENT_USER)
            .delete()
            .addOnSuccessListener {
                getLikesFromFirestore(postId, likesTextView)
                checkIfPostIsLiked(postId, likeIcon, unlikeIcon)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }


    suspend fun delete(postId: String){
        val imagesList: ArrayList<String> = arrayListOf()
        firebaseRepo.getDataToList(imagesList, FirebaseConst.POSTS + "$postId/images")
        viewModelScope.launch { for (image in imagesList) { firebaseRepo.deleteFromStorage(image, FirebaseConst.CURRENT_USER + "/posts/") } }
        firebaseRepo.delete(FirebaseConst.POSTS, postId)
    }
}
