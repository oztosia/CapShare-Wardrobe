package com.example.mypersonalwardrobe.constants

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FirebasePathsConstants {

    companion object {

        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_hh:mm:ss")
        val dirTimeStamp = SimpleDateFormat("yyyy.MM.dd").format(Date())
        val postTimeStamp = LocalDateTime.now().format(formatter)

        val USERS_PATH = "users"
        val CURRENT_USER =  FirebaseAuth.getInstance().currentUser?.uid.toString()
        val MY_ITEMS_PATH = "$USERS_PATH/$CURRENT_USER/items"
        val OBSERVED = "$USERS_PATH/$CURRENT_USER/observed"
        val STORAGE_POSTS_IMAGES = "$CURRENT_USER/posts"
        val POSTS = "posts/"
        val OUTFIT_ASKS = "outfit_asks/"


        val STORAGE_PATH = "$CURRENT_USER/"
        val CURRENT_USER_PATH = "$USERS_PATH/$CURRENT_USER"

        val HASHTAGS_PREFERENCES = "$CURRENT_USER_PATH/hashtags_preferences/"

        val OBSERVED_HASHTAGS = "observed"
        val BLOCKED_HASHTAGS = "blocked"


        val ALL_USERS = ""
        val OBSERVED_USERS = ""
    }
}
