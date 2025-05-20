package com.example.perla

import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController


class SharedViewModel : ViewModel() {
    var imageBitmap by mutableStateOf<Bitmap?>(null)
    var isNew by mutableStateOf(true)

}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CameraUI(nav: NavController,id:String, productName: String, price: String, description:String, sharedViewModel: SharedViewModel, destination : String) {
    fun addPhoto(bitmap: Bitmap) {
        sharedViewModel.imageBitmap = bitmap
        sharedViewModel.isNew=false
        nav.navigate("takenPhoto/${id}/${productName}/${price}/${description}/${destination}")
    }

    BackHandler {
        nav.navigate("${destination}/${id}/${productName}/${price}/${description}")
    }

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )

        // Cuadro de recorte (100x100)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier) {
                val width = 250.dp.toPx()
                val height = 250.dp.toPx()
                val startX = (size.width - width) / 2
                val startY = (size.height - height) / 2

                drawRect(
                    color = Color.White,
                    topLeft = androidx.compose.ui.geometry.Offset(startX, startY),
                    size = androidx.compose.ui.geometry.Size(width, height),
                    style = Stroke(width = 4f)
                )
            }
        }

        // Botón para cambiar de cámara
        Box(
            modifier = Modifier
                .offset(16.dp, 16.dp)
                .size(80.dp)
                .background(Color.White, shape = CircleShape), // Fondo blanco
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier.size(64.dp) // Tamaño interno del botón
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch camera",
                    tint = Color.Black, // Ícono oscuro sobre fondo claro
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        // Botón para tomar foto
        Box(
            modifier = Modifier
                .then(
                    if (isLandscape())
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp)
                    else
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        takePhoto(controller, context) { bitmap ->
                            addPhoto(bitmap)
                        }
                    },
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Take photo",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(36.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalContext.current.resources.configuration
    return configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
}


fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }

                val originalBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                val width = 1000
                val height = 1000
                val centerX = (originalBitmap.width - width) / 2
                val centerY = (originalBitmap.height - height) / 2

                val croppedBitmap = Bitmap.createBitmap(
                    originalBitmap,
                    centerX.coerceAtLeast(0),
                    centerY.coerceAtLeast(0),
                    width.coerceAtMost(originalBitmap.width),
                    height.coerceAtMost(originalBitmap.height)
                )

                onPhotoTaken(croppedBitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}
@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}
