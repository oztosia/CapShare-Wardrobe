package com.oztosia.capsharewardrobe.interfaces

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

public class BitmapToUriConverter {

    companion object BitmapToUriConverter{


        fun bitmapToUri(imageBitmap: Bitmap, context: Context): Uri {
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.WEBP, 30, baos)
            val baosToByteArray = baos.toByteArray()
            val file = File(context.cacheDir, imageBitmap.toString())
            file.delete()
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(baosToByteArray)
            fos.flush()
            fos.close()
            baos.close()
            return file.toUri()
        }
    }
}
