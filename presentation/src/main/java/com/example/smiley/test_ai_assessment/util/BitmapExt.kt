package com.example.smiley.test_ai_assessment.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.smiley.test_ai_assessment.camera.GraphicOverlay
import java.io.FileOutputStream


interface BitmapSavedCallback{
    fun onSaved(path:String)
}

/**
 * 비트맵을 캔버스에 그리는 메소드
 * @param overlay GraphicOverlay
 * @param bitmap Bitmap
 * @param isHorizontalMode Boolean
 */
fun Bitmap.drawBitmapOnCanvas(overlay: GraphicOverlay, isHorizontalMode: Boolean) {
    val canvas = overlay.processCanvas
    val paint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER) }

    canvas.drawBitmap(
        this,
        0f,
        this.getBaseYByView(overlay, isHorizontalMode),
        paint
    )
}

/**
 * Bitmap을 갤러리에 저장한 후 해당 경로를 ViewModel에 저장
 * 결과 화면에서 경로 변수에 Observer 걸어서 감지
 */
fun Bitmap.saveToGallery(context: Context, saveCallback: BitmapSavedCallback) {
    val path = makeTempFile().apply {
        FileOutputStream(this).run {
            this@saveToGallery.compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()
        }
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also {
            it.data = Uri.fromFile(this)
            context.sendBroadcast(it)
        }
    }.path

    saveCallback.onSaved(path)

    Log.d("사진 경로", path)
}


fun Bitmap.rotateFlipImage(degree: Float, isFrontMode: Boolean): Bitmap? {
    val realRotation = when (degree) {
        0f -> 90f
        90f -> 0f
        180f -> 270f
        else -> 180f
    }
    val matrix = Matrix().apply {
        if (isFrontMode) {
            preScale(-1.0f, 1.0f)
        }
        postRotate(realRotation)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.scaleImage(view: View, isHorizontalRotation: Boolean): Bitmap? {
    val ratio = view.width.toFloat() / view.height.toFloat()
    val newHeight = (view.width * ratio).toInt()

    return when (isHorizontalRotation) {
        true -> Bitmap.createScaledBitmap(this, view.width, newHeight, false)
        false -> Bitmap.createScaledBitmap(this, view.width, view.height, false)
    }
}

fun Bitmap.getBaseYByView(view: View, isHorizontalRotation: Boolean): Float {
    return when (isHorizontalRotation) {
        true -> (view.height.toFloat() / 2) - (this.height.toFloat() / 2)
        false -> 0f
    }
}