package com.example.mypersonalwardrobe.models

import java.util.*
import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class Post(
    var id: String,
    var date: String,
    var authorUid: String,
   // var hashtags: String,
    var text: String,
    //var imagesList: ArrayList<Photo>,
    //var likes: String
){
    @ParcelConstructor constructor(): this("", "", "",
        "")

}