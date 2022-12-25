package com.example.mypersonalwardrobe.models

import java.util.*

data class Message(
    var date: Date,
    var authorUid: String,
    var text: String,
    var imageUri: String,
) {
}