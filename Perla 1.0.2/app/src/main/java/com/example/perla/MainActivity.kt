@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.perla
import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perla.ui.theme.GreenAdd
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.perla.ui.theme.BlueMain
import com.example.perla.ui.theme.GreenMain
import com.example.perla.ui.theme.PerlaTheme
import com.example.perla.ui.theme.PurpleMain
import com.example.perla.ui.theme.RedMain
import com.example.perla.ui.theme.YellowMain
import kotlinx.coroutines.CoroutineScope
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val preferencesHelper = PreferencesHelper(context)
            val isDarkModeEnabled = remember { mutableStateOf(preferencesHelper.isDarkModeEnabled()) }
            val primaryColorKey = remember { mutableStateOf(preferencesHelper.getPrimaryColorKey()) }
            val coroutineScope = rememberCoroutineScope()

            PerlaTheme(
                darkTheme = isDarkModeEnabled.value,
                dynamicColor = false,
                primaryColorKey = primaryColorKey.value
            ) {
                UIPrincipal(
                    isDarkModeEnabled,
                    primaryColorKey,
                    coroutineScope,
                    preferencesHelper
                )
            }
        }
    }

    /*fun camPermisos() {
        ActivityCompat.requestPermissions(
            this, CAMERAX_PERMISSIONS, 0
        )
    }


    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }*/
}

@Composable
fun UIPrincipal(
    isDarkModeEnabled: MutableState<Boolean>,
    primaryColorKey: MutableState<String>,
    coroutineScope: CoroutineScope,
    preferencesHelper: PreferencesHelper
) {
    val sharedViewModel: SharedViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "VistaPrincipal") {
        composable("VistaPrincipal") {
            VistaPrincipal(
                navController,
                isDarkModeEnabled,
                primaryColorKey,
                coroutineScope,
                preferencesHelper
            )
        }

        composable("Editar/{id_producto}/{productName}/{price}/{description}"){
                backStackEntry -> Edicion(
            backStackEntry.arguments?.getString("id_producto")?:
            "Sin Id",navController, sharedViewModel
        )
        }

        composable("CamaraPrev/{id_producto}/{productName}/{price}/{description}/{destination}"){
            backStackEntry ->
                val id = backStackEntry.arguments?.getString("id_producto")?: " "
                val productName = backStackEntry.arguments?.getString("productName")?: " "
                val price = backStackEntry.arguments?.getString("price")?: " "
                val description = backStackEntry.arguments?.getString("description")?: " "
                val destination = backStackEntry.arguments?.getString("destination")?: " "
            CameraUI(navController,id,productName,price,description,sharedViewModel,destination)

        }
        composable("AgregarProducto/{id_producto}/{productName}/{price}/{description}") {
            backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")?: "a"
            val price = backStackEntry.arguments?.getString("price")?: " a"
            val description = backStackEntry.arguments?.getString("description")?: " a"

            AgregarProducto(navController,productName,price,description,sharedViewModel)
        }

        composable("takenPhoto/{id_producto}/{productName}/{price}/{description}/{destination}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id_producto")?: " "
            val productName = backStackEntry.arguments?.getString("productName") ?: "a"
            val price = backStackEntry.arguments?.getString("price") ?: " a"
            val description = backStackEntry.arguments?.getString("description") ?: " a"
            val destination = backStackEntry.arguments?.getString("destination")?: " "
            Confirmacion(navController,id,productName,price,description,sharedViewModel,destination)
        }

        composable("helpWindow") { helpWindow(navController)  }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun VistaPrincipal(
    navController: NavController,
    isDarkModeEnabled: MutableState<Boolean>,
    primaryColorKey: MutableState<String>,
    coroutineScope: CoroutineScope,
    preferencesHelper: PreferencesHelper
) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val dbHelper = DBHelper(context)
    var contador = rememberSaveable { mutableStateOf(0) }

    BackHandler {
        activity?.finish()
    }

    val colorMap = mapOf(
        "Yellow" to YellowMain,
        "Purple" to PurpleMain,
        "Red" to RedMain,
        "Blue" to BlueMain,
        "Green" to GreenMain
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        Text(
                            "Opciones",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    // Items para cambiar color
                    item {
                        Text(
                            "Cambiar color",
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    items(colorMap.keys.toList()) { colorKey ->
                        val resolvedColor = colorMap[colorKey] ?: YellowMain
                        NavigationDrawerItem(
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(resolvedColor, shape = CircleShape)
                                )
                            },
                            label = { Text(colorKey) },
                            selected = primaryColorKey.value == colorKey,
                            onClick = {
                                primaryColorKey.value = colorKey
                                preferencesHelper.setPrimaryColorKey(colorKey)
                                coroutineScope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp))
                    }

                    item {
                        NavigationDrawerItem(
                            icon = {
                                Image(
                                    painter = painterResource(
                                        id = if (isDarkModeEnabled.value) R.drawable.dom else R.drawable.luna
                                    ),
                                    contentDescription = "Cambiar tema",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(2.dp)
                                )
                            },
                            label = {
                                Text(
                                    if (isDarkModeEnabled.value) "Cambiar a modo claro" else "Cambiar a modo oscuro",
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                            selected = false,
                            onClick = {
                                coroutineScope.launch {
                                    val newTheme = !isDarkModeEnabled.value
                                    isDarkModeEnabled.value = newTheme
                                    preferencesHelper.setDarkModeEnabled(newTheme)
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    item {
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Help,
                                    contentDescription = "Ícono de ayuda",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            label = { Text("Ayuda") },
                            selected = false,
                            onClick = {
                                navController.navigate("helpWindow")
                                coroutineScope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp))
                    }

                    item {
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.SupportAgent,
                                    contentDescription = "Soporte técnico",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            label = { Text("Soporte técnico") },
                            selected = false,
                            onClick = {
                                val phoneNumber = "+5212461806235" // Número en formato internacional sin espacios
                                val message = "Hola. Tengo un problema con mi aplicación Tacos a la Linux"
                                val url = "https://wa.me/${phoneNumber.replace("+", "")}?text=${java.net.URLEncoder.encode(message, "UTF-8")}"
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                    data = url.toUri()
                                }

                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
                                }

                                coroutineScope.launch { drawerState.close() }
                            }
                            ,
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }

        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary).height(70.dp)
            ) {
                // Icono de menú para abrir el drawer
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu, // Usa Icons.Default.Menu si prefieres
                        contentDescription = "Abrir menú",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Logo
                Box{

                }
                Image(
                    painter = painterResource(id = R.drawable.linux_logo),

                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 50.dp)
                        .width(55.dp)
                        .height(55.dp)
                        .clickable {
                            if(contador.value <= 3){
                                Toast.makeText(context, "${contador.value+1}", Toast.LENGTH_SHORT).show()
                                contador.value++
                            }
                            else{
                                MediaPlayer
                                    .create(context,R.raw.linux_sound)
                                    .start()
                                contador.value = 0
                            }
                        }
                )

                // Título
                Text(
                    text = "Tacos a la Linux",
                    fontSize = 26.sp,
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // Botón de agregar producto
                Button(
                    onClick = {
                        navController.navigate("AgregarProducto////")
                    },
                    modifier = Modifier
                        .width(60.dp)
                        .height(50.dp)
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenAdd),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "+",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Menú inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            ) {
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
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider()

                // Lista de productos
                VistaNombres(dbHelper, navController)
            }
        }
    }
}

fun getColorFromKey(colorKey: String): Color {
    return when (colorKey) {
        "Red" -> RedMain
        "Blue" -> BlueMain
        "Green" -> GreenMain
        else -> PurpleMain
    }
}

@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    //UIPrincipal()
}