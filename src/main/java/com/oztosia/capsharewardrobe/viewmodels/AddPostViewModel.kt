package com.oztosia.capsharewardrobe.viewmodels

import GenericAdapter
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oztosia.capsharewardrobe.domain.FirebasePostsRepo
import com.oztosia.capsharewardrobe.domain.FirebaseUserRepo
import com.oztosia.capsharewardrobe.singletons.ItemsListHolder

class AddPostViewModel: ViewModel() {

    private val userRepo: FirebaseUserRepo = FirebaseUserRepo()
    private val postsRepo: FirebasePostsRepo = FirebasePostsRepo()

    val  _profileImageMutableLiveData = MutableLiveData<String>()

    fun getProfileImageMutableLiveData(): MutableLiveData<String> {
        userRepo.getProfileImageMutableLiveData(_profileImageMutableLiveData)
        return _profileImageMutableLiveData
    }

    fun addPost(text: String, hashtags: String, progressBar: ProgressBar){ postsRepo.addPost(text, hashtags, progressBar) }

    fun getDataFromPhotoListToRecyclerView(adapter: GenericAdapter<String>){ postsRepo.getDataFromPhotoListToRecyclerView(adapter) }

    fun deleteImage(uri: String){ ItemsListHolder.list.remove(uri) }
}