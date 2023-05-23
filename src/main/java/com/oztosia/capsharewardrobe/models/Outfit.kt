package com.oztosia.capsharewardrobe.models

import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class Outfit (var authorUid: String, var downloadURL: String, var uid: String, var docId: String, var askId: String)
{
    @ParcelConstructor
    constructor(): this("", "", "", "", "")

}