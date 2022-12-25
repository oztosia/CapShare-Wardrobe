package com.example.mypersonalwardrobe.models

import java.util.*
import kotlin.collections.ArrayList

data class Outfit (
    var date: Date,
    var authorUid: String,
    var imagesList: ArrayList<String>,
    var description: String
){
}