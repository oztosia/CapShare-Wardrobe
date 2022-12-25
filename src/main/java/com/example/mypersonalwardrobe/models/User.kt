package com.example.mypersonalwardrobe.models


import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class User(
    var userName: String,
    var email: String,
    var hashtags: String,
    var profileImage: String,
    var uid: String,
    var bio: String,
){
    @ParcelConstructor constructor(): this("", "",
        "","",
        "", ""
    )

}