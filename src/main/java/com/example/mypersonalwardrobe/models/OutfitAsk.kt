package com.example.mypersonalwardrobe.models

import java.util.*
import kotlin.collections.ArrayList

data class OutfitAsk(
    var date: Date, // vm
    var authorUid: String, //vm
    var hashtagsList: ArrayList<String>, // convert to list from string
    var title: String, //m
    var text: String, // m
    var imagesList: ArrayList<Photo> //m
){
}