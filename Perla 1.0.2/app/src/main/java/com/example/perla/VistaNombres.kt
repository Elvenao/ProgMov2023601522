package com.example.perla

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.perla.ui.theme.DeleteRed
import com.example.perla.ui.theme.EditBlue
import com.example.perla.ui.theme.Gris

@SuppressLint("DefaultLocale")
@Composable
fun VistaNombres(dbHelper: DBHelper, navController: NavController) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val nombres = remember {
        mutableStateOf(dbHelper.obtenerNombres()) // Lista de arreglos
    }
    val img = ImgUtilities()
    val idParaEliminar = remember { mutableStateOf<String?>(null) }

    LazyColumn(Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)) {
        items(nombres.value) { producto ->
            Row(
                Modifier
                    .padding(18.dp)
                    .fillMaxWidth() ){
                val base64String: String = producto[4].toString()
                val bitmap = img.decodeBase64ToImageBitmap(base64String)
                val id = producto[0]

                Box(Modifier.align(Alignment.CenterVertically)) {
                    bitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "imagen",
                            modifier = Modifier
                                .size(130.dp)

                        )
                    }
                }
                Column(Modifier.padding(start = 20.dp)){
                    Row {
                        Text(
                            text = "${producto[1]}",
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left
                        )

                    }
                    Row {
                        Text(
                            text = "${producto[3]}",
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left,
                            lineHeight = 18.sp

                        )
                    }
                    Row {
                        Text(
                            text = "$${String.format("%.2f", producto[2].toString().toDouble())}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly ){
                        Button(onClick = {
                            navController.navigate("Editar/$id///")
                            //Toast.makeText(context, "Editar coming soon", Toast.LENGTH_SHORT).show()
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background, // Color dinámico basado en el theme
                            contentColor = MaterialTheme.colorScheme.onPrimary // Color del contenido (texto o ícono)
                        )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.edit_icon),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }
                        Button(
                            onClick = {
                                idParaEliminar.value = id.toString()  // Guarda el ID seleccionado
                                showDialog.value = true
                            },
                            Modifier.padding(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background, // Color dinámico basado en el theme
                                contentColor = MaterialTheme.colorScheme.onPrimary // Color del contenido (texto o ícono)
                            )

                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.delete_icon2),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }

                    }
                }
            }
            HorizontalDivider(
                color = Gris,
                thickness = 1.5.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }


    }
    //Alert dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text = "Confirmar acción")
            },
            text = {
                Text("¿Estás seguro de que deseas realizar esta acción?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        idParaEliminar.value?.let { id ->
                            dbHelper.borrarProducto(id.toInt())
                            nombres.value = dbHelper.obtenerNombres()
                            showDialog.value = false
                            Toast.makeText(context, "Borrado correctamente", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeleteRed)
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EditBlue)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


}