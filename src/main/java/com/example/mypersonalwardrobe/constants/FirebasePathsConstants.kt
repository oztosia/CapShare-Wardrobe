package com.example.mypersonalwardrobe.constants

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FirebasePathsConstants {

    companion object {

        val dirTimeStamp = SimpleDateFormat("yyyy.MM.dd").format(Date())
        val postTimeStamp = SimpleDateFormat("yyyy.MM.dd_hh:mm:ss").format(Date())

        val USERS_PATH = "users"
        val CURRENT_USER =  FirebaseAuth.getInstance().currentUser?.uid
        val MY_ITEMS_PATH = "$USERS_PATH/$CURRENT_USER/items"
        val OBSERVED = "$USERS_PATH/$CURRENT_USER/observed"
        val STORAGE_POSTS_IMAGES = "$CURRENT_USER/posts"
        val MY_POST_PATH ="posts/$CURRENT_USER/$dirTimeStamp"
        val MY_FIRESTORE_POSTS_IMAGES = "$MY_POST_PATH/$postTimeStamp/images"
        val POSTS = "posts"


        val ALL_USERS = ""
        val OBSERVED_USERS = ""
    }
}
