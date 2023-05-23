package com.oztosia.capsharewardrobe.constants

import com.google.firebase.auth.FirebaseAuth


class FirebaseConst {

    companion object {

        val USERS_PATH = "users"
        val CURRENT_USER =  FirebaseAuth.getInstance().currentUser?.uid.toString()
        val MY_ITEMS_PATH = "$USERS_PATH/$CURRENT_USER/items"
        val OBSERVED = "$USERS_PATH/$CURRENT_USER/observed"
        val STORAGE_POSTS_IMAGES = "$CURRENT_USER/posts"
        val STORAGE_CHAT_IMAGES = "$CURRENT_USER/chatImages"
        val STORAGE_OUTFITS = "$CURRENT_USER/outfits"
        val POSTS = "posts/"
        val OUTFIT_ASKS = "outfit_asks/"
        val CHAT = "chat/"


        val STORAGE_PATH = "$CURRENT_USER/"
        val CURRENT_USER_PATH = "$USERS_PATH/$CURRENT_USER"

        val HASHTAGS_PREFERENCES = "$CURRENT_USER_PATH/hashtags_preferences/"
        val BIO = "$CURRENT_USER_PATH/bio/"

        val OBSERVED_HASHTAGS = "observed"
        val BLOCKED_HASHTAGS = "blocked"

        val POST_HASHTAGS = "post_hashtags"
    }
}
