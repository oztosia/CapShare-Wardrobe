package com.example.mypersonalwardrobe.models

import java.util.*

data class Post(
    var date: Date,
    var authorUid: String,
    var hashtags: String,
    var text: String,
    var imagesList: ArrayList<Photo>,
    var likes: Int
){
}