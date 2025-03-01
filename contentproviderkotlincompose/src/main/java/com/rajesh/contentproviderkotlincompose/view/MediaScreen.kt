package com.rajesh.contentproviderkotlincompose.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import com.rajesh.contentproviderkotlincompose.viewmodel.MediaViewModel

@Composable
fun MediaScreen(viewModel: MediaViewModel) {
    val mediaItems by viewModel.mediaItems.collectAsState()
    val uploadStatus by viewModel.uploadStatus.collectAsState()

    LaunchedEffect(Unit) {
//        viewModel.loadMedia("".toUri())
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(mediaItems) { uri ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { viewModel.uploadMedia(uri) }
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth(),
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components { add(VideoFrameDecoder.Factory()) }
                            .build()
                    )
                }
            }
        }
        
        uploadStatus.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}