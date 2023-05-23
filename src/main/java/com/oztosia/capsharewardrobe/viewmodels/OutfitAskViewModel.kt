package com.oztosia.capsharewardrobe.viewmodels

import GenericAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.source.FirebaseGenericRepo
import com.oztosia.capsharewardrobe.domain.FirebaseOutfitAsksRepo
import com.oztosia.capsharewardrobe.domain.FirebaseUserRepo
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder.Singleton.list
import com.oztosia.capsharewardrobe.models.OutfitAsk
import com.oztosia.capsharewardrobe.models.Photo
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.oztosia.capsharewardrobe.models.Outfit

class OutfitAskViewModel: ViewModel(){

    private val firebaseRepo: FirebaseGenericRepo = FirebaseGenericRepo()
    private val outfitAskRepo: FirebaseOutfitAsksRepo = FirebaseOutfitAsksRepo()
    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private var  _profileImageMutableLiveData = MutableLiveData<String>()
    var  _lastDocumentSnapshotMutableLiveData = MutableLiveData<DocumentSnapshot>()

    fun getMyOutfitAsksFromFirestoreToRecyclerView(adapter: GenericAdapter<OutfitAsk>) { outfitAskRepo.getMyOutfitsAsksToRecyclerView(adapter) }

    fun getOutfitAsksFromFirestoreToRecyclerView(adapter: GenericAdapter<OutfitAsk>) { outfitAskRepo.getOutfitsAsksToRecyclerView(adapter) }

    fun getOutfitAskImagesFromFirestoreToRecyclerView(adapter: GenericAdapter<Photo>, id: String) { firebaseRepo.getDataToRecyclerView(adapter, FirebaseConst.OUTFIT_ASKS + "$id/images") }

    fun deleteImageUriFromPhotoList(uri: String){ list.remove(uri) }

    fun addOutfitAsk(title: String, text: String, hashtags: String){ outfitAskRepo.addOutfitAsk(title, text, hashtags) }

    fun getHashtagsDataFromFirestoreToRecyclerView(adapter: GenericAdapter<String>, outfitAskId: String) { firebaseRepo.getFromDocumentAndSplit(FirebaseConst.OUTFIT_ASKS, outfitAskId, "hashtags", adapter) }

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

    fun getProfileImageMutableLiveData(): MutableLiveData<String> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        return _profileImageMutableLiveData
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>){
        adapter.dataSet.addAll(list)
        adapter.notifyDataSetChanged()
    }

    fun delete(id: String){
        firebaseRepo.delete(FirebaseConst.OUTFIT_ASKS, id)
    }

    fun getResponses(id: String, adapter: GenericAdapter<Outfit>){ firebaseRepo.getDataToRecyclerView(adapter, FirebaseConst.OUTFIT_ASKS + "$id/responses") }


}