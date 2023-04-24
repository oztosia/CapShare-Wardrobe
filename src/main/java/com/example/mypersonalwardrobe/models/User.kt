package com.example.mypersonalwardrobe.models


import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class User(
    var userName: String,
    var hashtags: String,
    var profileImage: String,
    var uid: String){
    @ParcelConstructor constructor(): this("", "",
        "","")

}