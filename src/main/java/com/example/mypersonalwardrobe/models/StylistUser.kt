package com.example.mypersonalwardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class StylistUser @ParcelConstructor constructor(
    var userName: String ,
    var profileImage: String,
    var uid: String,
    var bio: String,
    var hashtags: String
     ){
}