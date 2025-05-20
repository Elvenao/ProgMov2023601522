package com.example.perla

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.perla.ui.theme.DeleteRed
import com.example.perla.ui.theme.GreenAdd
import com.example.perla.ui.theme.Gris
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.saveable.rememberSaveable


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("DefaultLocale")
@Composable
fun AgregarProducto(navController: NavController, productName:String, price:String, description: String, sharedViewModel: SharedViewModel){
    val context = LocalContext.current
    val imagen = if (sharedViewModel.isNew) {
        BitmapFactory.decodeResource(context.resources, R.drawable.camera_512)
    } else {
        sharedViewModel.imageBitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.camera_512)
    }

    BackHandler {
        sharedViewModel.imageBitmap=null
        sharedViewModel.isNew=true
        navController.navigate("VistaPrincipal")
    }

    var nombre by rememberSaveable { mutableStateOf(productName) }
    var precio by rememberSaveable { mutableStateOf(price) }
    var descripcion by rememberSaveable { mutableStateOf(description) }
    var lastKeyWasDel by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var invalidData by rememberSaveable { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navController.navigate("CamaraPrev//${nombre}/${precio}/${descripcion}/AgregarProducto")
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
        }
    }


    val focusRequesterPrecio = remember { FocusRequester() }
    val focusRequesterDescripcion = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Column(
            Modifier.fillMaxSize().padding(bottom = 90.dp).background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        MaterialTheme.colorScheme.primary
                    ),
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    "Agregar Producto",
                    Modifier.align(Alignment.CenterVertically),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(
                color = Gris,
                thickness = 1.5.dp,
                modifier = Modifier.fillMaxWidth()
                    .padding(0.dp)
            )
            Row(Modifier.height(160.dp).padding(bottom = 20.dp)) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Toca para añadir",

                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Button(
                        onClick = {
                            val hasCameraPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasCameraPermission) {
                                navController.navigate("CamaraPrev//${nombre}/${precio}/${descripcion}/AgregarProducto")
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            bitmap = imagen.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                        )
                    }

                }
                Box(Modifier.align(Alignment.CenterVertically)) {
                    Row(Modifier.padding(start = 20.dp)) {
                        Text(
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "${nombre.length} / 30"
                        )
                    }
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            if (it.length <= 30 || lastKeyWasDel) {
                                nombre = it
                            }
                        },
                        label = { Text("Producto") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
                            .focusRequester(focusRequesterPrecio),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusRequesterDescripcion.requestFocus()
                            }
                        )
                    )
                }

            }

            Row(Modifier.padding(start = 20.dp)) {
                Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${descripcion.length} / 100"
                )
            }
            Row(Modifier.height(120.dp)) {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        if (it.length <= 100 || lastKeyWasDel) {
                            descripcion = it
                        }
                    },
                    label = { Text("Descripcion") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Bottom)
                        .padding(start = 15.dp, end = 15.dp)
                        .focusRequester(focusRequesterDescripcion)
                        .onKeyEvent { keyEvent ->
                            lastKeyWasDel = keyEvent.type == KeyEventType.KeyDown &&
                                    keyEvent.key == Key.Backspace
                            false // Deja que el evento continúe hacia el TextField
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusRequesterPrecio.requestFocus()
                        }
                    )
                )
            }
            Row(Modifier.padding(top = 20.dp)) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = { newText ->
                        var dotIncluded = false
                        val cleanIntegerPart = StringBuilder()
                        val cleanDecimalPart = StringBuilder()

                        for (char in newText) {
                            if (char.isDigit()) {
                                if (!dotIncluded) {
                                    if (cleanIntegerPart.length < 7) {
                                        cleanIntegerPart.append(char)
                                    }
                                } else {
                                    if (cleanDecimalPart.length < 2) {
                                        cleanDecimalPart.append(char)
                                    }
                                }
                            } else if (char == '.' && !dotIncluded) {
                                dotIncluded = true
                            }
                            // Ignorar otros caracteres
                        }

                        val newCleanText = if (dotIncluded) {
                            cleanIntegerPart.toString() + "." + cleanDecimalPart.toString()
                        } else {
                            cleanIntegerPart.toString()
                        }

                        precio = newCleanText
                    },
                    label = { Text("Precio") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .focusRequester(focusRequesterPrecio)
                        .onKeyEvent { keyEvent ->
                            lastKeyWasDel = keyEvent.type == KeyEventType.KeyDown &&
                                    keyEvent.key == Key.Backspace
                            false
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            val formattedPrice = try {
                                BigDecimal(precio).setScale(2, RoundingMode.HALF_UP).toPlainString()
                            } catch (e: Exception) {
                                "0.00"
                            }
                            precio = formattedPrice
                            keyboardController?.hide()
                        }
                    ),
                    singleLine = true
                )
            }


        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.background(MaterialTheme.colorScheme.background),horizontalArrangement = Arrangement.SpaceAround) {
                Button(
                    onClick = {
                        sharedViewModel.imageBitmap = null
                        sharedViewModel.isNew = true
                        navController.navigate("VistaPrincipal")
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteRed
                    )
                ) {
                    Text(
                        "Cancelar",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.width(35.dp))
                Button(
                    onClick = {
                        val precioAux = precio.ifEmpty { "0" }

                        val precioValido = precioAux.matches(Regex("^\\d{1,7}(\\.\\d{0,2})?$"))

                        if (precioValido && precioAux.toFloat() >= 0.00 && descripcion.isNotEmpty() && nombre.isNotEmpty() && sharedViewModel.imageBitmap != null) {
                            // Redondear a 2 decimales con BigDecimal
                            val precioRedondeado = String.format("%.2f", precioAux.toDouble())

                            precio =
                                precioRedondeado // lo actualiza también visualmente si quieres

                            showDialog = true
                        } else {
                            invalidData = true
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenAdd,
                    ),
                ) {
                    Text(
                        "Agregar",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(text = "Confirmar acción")
            },
            text = {
                Text("¿Estás seguro de que quieres agregar este producto?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        sharedViewModel.imageBitmap?.let { bitmap ->
                            insertDB(bitmap, nombre, precio, descripcion,context)
                            sharedViewModel.imageBitmap=null
                            sharedViewModel.isNew=true
                            navController.navigate("VistaPrincipal")
                        }
                    },colors = ButtonDefaults.buttonColors(
                        containerColor = GreenAdd,  // Color de fondo
                    )
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    },colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteRed,  // Color de fondo
                    )
                ){
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
    if(invalidData){
        AlertDialog(
            onDismissRequest = {
                invalidData = false
            },
            title = {
                Text(text = "Datos Invalidos")
            },
            text = {
                Text("Revisa tus valores o que se haya cargado la imagen correctamente.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        invalidData = false
                    },colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,  // Color de fondo
                    )
                ) {
                    Text("Continuar", color = MaterialTheme.colorScheme.onSurface)
                }
            }

        )
    }
}


fun insertDB(bitmap: Bitmap, productName: String, price: String, description: String,context: Context){
    val dbHelper = DBHelper(context)
    val imgUtilities = ImgUtilities()
    CoroutineScope(Dispatchers.IO).launch {
        dbHelper.insertarProducto(arrayOf(imgUtilities.bitmapToBase64(bitmap),productName,price,description,"1"),
            arrayOf("imagen,nombre,precio,descripcion,status")
        )
    }
}