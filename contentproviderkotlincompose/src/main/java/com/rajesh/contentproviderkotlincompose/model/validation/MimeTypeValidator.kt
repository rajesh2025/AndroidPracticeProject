package com.rajesh.contentproviderkotlincompose.model.validation

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

object MimeTypeValidator {
    private val allowedImageTypes = setOf(
        "image/jpeg",
        "image/png",
        "image/gif"
    )

    private val allowedVideoTypes = setOf(
        "video/mp4",
        "video/3gpp",
        "video/webm"
    )

    fun isValidMediaFile(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri) ?: return false
        return allowedImageTypes.contains(mimeType) || allowedVideoTypes.contains(mimeType)
    }

    fun getFileSize(context: Context, uri: Uri): Long {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            it.moveToFirst()
            it.getLong(it.getColumnIndexOrThrow(OpenableColumns.SIZE))
        } ?: 0L
    }
}

