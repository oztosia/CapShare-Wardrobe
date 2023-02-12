package com.example.mypersonalwardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
class Photo(
    var downloadURL: String,
    var hashtags: String,
    var docId: String,
){
    @ParcelConstructor constructor(): this("", "",
        "")

}