package com.example.perla

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perla.ui.theme.Bones
import com.example.perla.ui.theme.ColorTaco
import com.example.perla.ui.theme.DeleteRed
import com.example.perla.ui.theme.EditBlue
import com.example.perla.ui.theme.GreenAdd

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {


            UIPrincipal()
        }
    }
}

@Composable
fun UIPrincipal(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "VistaPrincipal"){
        composable("VistaPrincipal") { VistaPrincipal(navController) }

        composable("Editar/{id_producto}"){
                backStackEntry -> Edicion(
            backStackEntry.arguments?.getString("id_producto")?:
            "Sin Id",navController
        )
        }
    }
}

@Composable
fun VistaPrincipal(navController: NavController) {
    // Obtener el contexto actual
    val context = LocalContext.current
    val dbHelper = DBHelper(context)
    val showDialog = remember { mutableStateOf(false) }

    @Composable
    fun VistaNombres(dbHelper: DBHelper) {
        val nombres = remember {
            mutableStateOf(dbHelper.obtenerNombres()) 
        }
        val img = ImgUtilities()

        LazyColumn(Modifier.fillMaxWidth()) {
            items(nombres.value) { producto ->
                Row(Modifier.padding(18.dp).fillMaxWidth()) {
                    val base64String: String = producto[4].toString()
                    val bitmap = img.decodeBase64ToImageBitmap(base64String)

                    bitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "imagen",
                            modifier = Modifier
                                .size(120.dp)
                                .align(alignment = Alignment.CenterVertically)
                        )
                    }
                    Column(Modifier.padding(start = 20.dp)){
                        Row {
                            Text(
                                text = "${producto[1]}",
                                Modifier.width(200.dp).padding(bottom = 4.dp),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Medium
                            )

                        }
                        Row {
                            Text(
                                text = "$${String.format("%.2f", producto[2].toString().toDouble())}",
                                modifier = Modifier.width(200.dp).padding(bottom = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                        Row {
                            Text(
                                text = "${producto[3]}",
                                Modifier.width(200.dp).padding(bottom = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                        Row{
                            Button(onClick = {
                                // TODO: lógica cuando se cliquee el registro
                                navController.navigate("Editar/${producto[0].toString()}")
                                //Toast.makeText(context, "Editar coming soon", Toast.LENGTH_SHORT).show()
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = Bones,  // Color de fondo

                            )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.edit_icon),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .width(55.dp)
                                        .height(55.dp)

                                )
                            }
                            Button(onClick = {
                                // TODO: lógica cuando se cliquee el registro
                                showDialog.value=true
                            }, Modifier.padding(0.dp), colors = ButtonDefaults.buttonColors(
                                containerColor = Bones,  // Color de fondo
                            )) {
                                Image(
                                    painter = painterResource(id = R.drawable.delete_icon2),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .width(55.dp)
                                        .height(55.dp)
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 0.5.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }


        }

    }

    //Alert dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                //back
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
                        showDialog.value = false
                        Toast.makeText(context,"Borrar coming soon", Toast.LENGTH_SHORT).show()
                        // TODO: lógica cuando se cliquee el registro
                    },colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteRed,  // Color de fondo
                    )
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    },colors = ButtonDefaults.buttonColors(
                        containerColor = EditBlue,  // Color de fondo
                    )
                ){
                    Text("Cancelar")
                }
            }
        )
    }

    // Mostrar los datos de la base

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Bones)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(ColorTaco)
        ) {
            // Imagen alineada a la derecha
            Image(
                painter = painterResource(id = R.drawable.linux_logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(60.dp)
                    .height(60.dp)
            )

            // Texto centrado
            Text(
                text = "Tacos a la Linux",
                fontSize = 26.sp,
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    Toast.makeText(context,"Agregar coming soon", Toast.LENGTH_SHORT).show()
                    // TODO: lógica cuando se cliquee el registro
                },
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp), // cuadrado
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenAdd
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth() 
                )
            }

        }

        HorizontalDivider(
            color = Color.Gray,
            thickness = 0.5.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.Center 
        ) {
            Text(
                text = "Menú",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider()
        VistaNombres(dbHelper)
    }
}

@Composable
fun Edicion(id: String,navController: NavController) {
    val img = ImgUtilities()
    val context = LocalContext.current
    val dbHelper = DBHelper(context)

    
    val producto = remember { mutableStateOf<Array<Any>?>(null) }

    LaunchedEffect(id) {
        producto.value = dbHelper.obtenerNombre(id)
    }

    
     Column {
        if (producto.value != null) {

            val base64String: String = producto.value!![3].toString()
            val bitmap = img.decodeBase64ToImageBitmap(base64String)
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)
                .background(
                ColorTaco),
                horizontalArrangement = Arrangement.Center){

                Text(
                    "${producto.value!![0]}",
                    Modifier.align(Alignment.CenterVertically),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,

                )


            }
            HorizontalDivider(Modifier.padding(bottom = 25.dp))
            Row(Modifier.padding(start = 20.dp)) {

                bitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap,
                        contentDescription = "imagen",
                        modifier = Modifier
                            .size(120.dp)

                    )
                }

                Column(Modifier.padding(start = 20.dp, end = 10.dp)) {
                    Row(Modifier.padding(bottom = 10.dp)) {
                        Text("Precio: $${producto.value!![1]}", fontSize = 17.sp)
                    }
                    Row (Modifier.padding(bottom = 10.dp)){
                        Text("Descripcion: ${producto.value!![2]}", fontSize = 17.sp)
                    }


                }
            }

        } else {
            Text("Cargando...")
        }
         Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom)  {


             Button(onClick = { navController.popBackStack() },colors = ButtonDefaults.buttonColors(
                 containerColor = ColorTaco,  // Color de fondo
                 contentColor = Color.White      // Color del texto
             ) ) {
                 Text("Volver", fontSize = 20.sp, fontWeight = FontWeight.Bold)
             }
             Spacer(Modifier.padding(10.dp))
         }

    }
}


@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    UIPrincipal()
}
