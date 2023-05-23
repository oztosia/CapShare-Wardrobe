package com.oztosia.capsharewardrobe.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarCreator {

    companion object SnackbarCreator {
        fun show(resId: View, text: String){
            Snackbar.make(resId, text, Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.GRAY)
                .setTextColor(Color.WHITE)
                .show()
        }
    }
}