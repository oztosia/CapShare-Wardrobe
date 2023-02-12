package com.example.mypersonalwardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
class PostImage(

    var downloadURL: String,
    var docId: String
){
    @ParcelConstructor constructor(): this("", "")

}