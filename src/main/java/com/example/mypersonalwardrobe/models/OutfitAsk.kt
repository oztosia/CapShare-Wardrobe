package com.example.mypersonalwardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)

data class OutfitAsk(
    var date: String, // vm
    var id: String,
    var authorUid: String, //vm
    var hashtags: String, // convert to list from string
    var title: String, //m
    var text: String, // m
){
    @ParcelConstructor
    constructor(): this("", "",
        "", "", "", "")

}