package com.example.perla

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.perla.ui.theme.DeleteRed
import com.example.perla.ui.theme.GreenAdd

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Confirmacion(
    navController: NavController,
    id: String,
    productName: String,
    price: String,
    description: String,
    sharedViewModel: SharedViewModel,
    destination: String
) {
    BackHandler {
        sharedViewModel.imageBitmap = null
        sharedViewModel.isNew = true
        navController.navigate("${destination}/${id}/${productName}/${price}/${description}")
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        val isLandscape = maxWidth > maxHeight

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "Confirmación",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLandscape) {
                // Horizontal: botones a los lados de la imagen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 350.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BotonRechazar(navController, sharedViewModel, destination, id, productName, price, description)

                    sharedViewModel.imageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 200.dp, max = 400.dp)
                        )
                    }

                    BotonAceptar(navController, destination, id, productName, price, description)
                }
            } else {
                // Vertical: imagen arriba, botones abajo
                sharedViewModel.imageBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonRechazar(navController, sharedViewModel, destination, id, productName, price, description)
                    BotonAceptar(navController, destination, id, productName, price, description)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun BotonRechazar(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    destination: String,
    id: String,
    productName: String,
    price: String,
    description: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Rechazar", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        Button(
            onClick = {
                sharedViewModel.imageBitmap = null
                sharedViewModel.isNew = true
                navController.navigate("${destination}/${id}/${productName}/${price}/${description}")
            },
            modifier = Modifier
                .width(140.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeleteRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete_icon2),
                contentDescription = "Rechazar",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun BotonAceptar(
    navController: NavController,
    destination: String,
    id: String,
    productName: String,
    price: String,
    description: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Aceptar", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        Button(
            onClick = {
                navController.navigate("${destination}/${id}/${productName}/${price}/${description}")
            },
            modifier = Modifier
                .width(140.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenAdd),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.linux_logo),
                contentDescription = "Aceptar",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}





@Preview(showBackground = true)
@Composable
fun Prev() {
    val context = LocalContext.current
    val nav = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    // Cargar imagen de drawable como Bitmap
    val drawable = ContextCompat.getDrawable(context, R.drawable.linux_logo) // reemplaza con tu imagen
    val bitmap = drawable?.toBitmap()

    sharedViewModel.imageBitmap = bitmap

    Confirmacion(nav,"","Producto", "10.99", "Descripción de prueba", sharedViewModel,"AgregarProducto")
}