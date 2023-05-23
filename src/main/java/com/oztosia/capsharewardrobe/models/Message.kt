package com.oztosia.capsharewardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)

data class Message(
    var date: String,
    var authorUid: String,
    var text: String,
    var downloadUrl: String,
){
    @ParcelConstructor
    constructor(): this("", "",
        "", "")

}