package com.example.mypersonalwardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
class Photo @ParcelConstructor constructor(
    var uri: String ,
    var hashtags: String,
    var docId: String
){
}