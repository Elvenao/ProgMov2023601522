package com.example.perla

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
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
import com.example.perla.ui.theme.EditBlue
import com.example.perla.ui.theme.GreenAdd
import com.example.perla.ui.theme.Gris
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@SuppressLint("DefaultLocale")
@Composable
fun Edicion(id: String,navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val img = ImgUtilities()
    val dbHelper = DBHelper(context)
    var lastKeyWasDel by rememberSaveable { mutableStateOf(false) }
    val producto = remember { mutableStateOf<Array<Any>?>(null) }
    var productName by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var invalidData by rememberSaveable { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navController.navigate("CamaraPrev/${id}/${productName}/${price}/${description}/Editar")
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
        }
    }
    val focusRequesterPrecio = remember { FocusRequester() }
    val focusRequesterDescripcion = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current


    BackHandler {
        sharedViewModel.imageBitmap=null
        sharedViewModel.isNew=true
        navController.navigate("VistaPrincipal")
    }
    LaunchedEffect(id) {
            producto.value = dbHelper.obtenerNombre(id)
        if(productName.isEmpty()) productName = producto.value!![0].toString()
        //if (price.isEmpty()) price = producto.value!![1].toString()
        if (price.isEmpty()) {
            price = try {
                String.format("%.2f", producto.value!![1].toString().toDouble())
            } catch (e: Exception) {
                "0.00"
            }
        }
        if (description.isEmpty()) description = producto.value!![2].toString()

    }

    // Mostrar la información del producto si se ha cargado
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(Modifier.fillMaxSize().padding(bottom = 90.dp).background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
            if (producto.value != null) {
                if (sharedViewModel.isNew) {
                    val base64String: String = producto.value!![3].toString()
                    sharedViewModel.imageBitmap = img.decode64ToBitmap(base64String)
                }
                val bitmap = sharedViewModel.imageBitmap ?: BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.camera_512
                )
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
                        "Editar ${productName}",
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
                Row(Modifier.padding(bottom = 20.dp).height(170.dp)) {
                    Box(
                        modifier = Modifier
                            .height(170.dp)
                            .width(170.dp)
                            .padding(top = 40.dp)
                    ) {
                        bitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "imagen",
                                modifier = Modifier
                                    .size(140.dp)
                                    .matchParentSize(),
                            )
                        }
                        Button(
                            onClick = {
                                val hasCameraPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasCameraPermission) {
                                    navController.navigate("CamaraPrev/${id}/${productName}/${price}/${description}/Editar")
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier.align(Alignment.BottomCenter),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = EditBlue
                            )

                        ) {
                            Text("Editar", color=White)
                        }
                    }
                    Column(Modifier.align(Alignment.CenterVertically)){
                        Row() {
                            Text(
                                color = MaterialTheme.colorScheme.onSurface,
                                text = "${productName.length} / 30"
                            )
                        }
                        Row{
                            OutlinedTextField(
                                value = productName,
                                onValueChange = {
                                    if (it.length <= 30 || lastKeyWasDel) {
                                        productName = it
                                    }
                                },
                                label = { Text("Nombre del Producto") },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .padding(end = 15.dp)
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



                }
                Row(Modifier.padding(start = 15.dp)) {
                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        text = "${description.length} / 100"
                    )
                }
                Row(Modifier.padding(bottom = 10.dp)) {
                    Text("")
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            if (it.length <= 100 || lastKeyWasDel) {
                                description = it
                            }
                        },
                        label = { Text("Descripción") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.Bottom)
                            .padding(start = 15.dp, end = 15.dp)
                            .focusRequester(focusRequesterDescripcion)
                            .onKeyEvent { keyEvent ->
                                lastKeyWasDel = keyEvent.type == KeyEventType.KeyDown &&
                                        keyEvent.key == Key.Backspace
                                false
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
                        value = price,
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

                            price = newCleanText
                        }
                        ,
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
                                    BigDecimal(price).setScale(2, RoundingMode.HALF_UP)
                                        .toPlainString()
                                } catch (e: Exception) {
                                    "0.00"
                                }
                                price = formattedPrice
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true
                    )
                }



            } else {
                Text("Cargando...", color = MaterialTheme.colorScheme.onPrimary,)
            }

            if (showDialog) {
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
                                    updateDB(
                                        productName,
                                        price,
                                        description,
                                        id,
                                        img.bitmapToBase64(bitmap),
                                        context
                                    )
                                    sharedViewModel.imageBitmap = null
                                    sharedViewModel.isNew = true
                                    navController.navigate("VistaPrincipal")
                                }
                            }, colors = ButtonDefaults.buttonColors(
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
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = DeleteRed,  // Color de fondo
                            )
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
            if (invalidData) {
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
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,  // Color de fondo
                            )
                        ) {
                            Text("Continuar", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Button(
                    onClick = {
                        sharedViewModel.imageBitmap = null
                        sharedViewModel.isNew = true
                        navController.navigate("VistaPrincipal")
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteRed
                    )
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface,)
                }
                Spacer(Modifier.width(35.dp))
                Button(
                    onClick = {
                        val precioAux = if (price.isEmpty()) "0" else price

                        val precioValido = precioAux.matches(Regex("^\\d{1,7}(\\.\\d{0,2})?$"))

                        if (precioValido && precioAux.toFloat() >= 0.00 && description.isNotEmpty() && productName.isNotEmpty() && sharedViewModel.imageBitmap != null) {
                            // Redondear a 2 decimales con BigDecimal
                            val precioRedondeado = String.format("%.2f", precioAux.toDouble())

                            price =
                                precioRedondeado
                            showDialog = true
                        } else {
                            invalidData = true
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditBlue,  // Color de fondo
                    ),
                ) { Text("Editar", color = MaterialTheme.colorScheme.onSurface,) }
            }
        }
    }
}

fun updateDB(productName: String, price: String, description: String,id:String, base64 : String, context: Context){
    val dbHelper = DBHelper(context)
    CoroutineScope(Dispatchers.IO).launch {
        dbHelper.actualizarProducto("producto",
            arrayOf("nombre","precio","descripcion","imagen"),arrayOf(productName,price,description,base64,id)
        )

    }
}