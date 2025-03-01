package com.rajesh.contentproviderkotlincompose.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajesh.contentproviderkotlincompose.model.content.MediaContentProvider
import com.rajesh.contentproviderkotlincompose.model.network.NetworkClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val mediaContentProvider: MediaContentProvider) : ViewModel() {
    private val _mediaItems = MutableStateFlow<List<Uri>>(emptyList())
    val mediaItems: StateFlow<List<Uri>> = _mediaItems.asStateFlow()

    private val _uploadStatus = MutableStateFlow<String>("")
    val uploadStatus: StateFlow<String> = _uploadStatus.asStateFlow()

    fun loadMedia(uri: Uri) {
        viewModelScope.launch {
            val cursor = mediaContentProvider.query(
                uri,
                null,
                null,
                null,
                "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
            )
            cursor?.use {
                val uriIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val uris = mutableListOf<Uri>()
                while (it.moveToNext()) {
                    val path = it.getString(uriIndex)
                    uris.add(Uri.fromFile(File(path)))
                }
                _mediaItems.value = uris
            }
        }
    }

    fun uploadMedia(uri: Uri) {
        viewModelScope.launch {
            try {
                val file = File(uri.path!!)
                NetworkClient.uploadMedia(file)
                _uploadStatus.value = "Upload successful"
            } catch (e: Exception) {
                _uploadStatus.value = "Upload failed: ${e.message}"
            }
        }
    }

}