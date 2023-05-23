package com.oztosia.capsharewardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class Post(
    var id: String,
    var date: String,
    var hashtags: String,
    var authorUid: String,
    var text: String,
    var username: String,
    var profileImage: String
){
    @ParcelConstructor constructor(): this("", "", "",
        "", "", "", "")

}