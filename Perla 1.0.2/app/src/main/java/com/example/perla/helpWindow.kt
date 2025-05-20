package com.example.perla

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.perla.ui.theme.DeleteRed
import com.example.perla.ui.theme.EditBlue
import com.example.perla.ui.theme.GreenAdd

@Composable
fun helpWindow(navController: NavController) {
    BackHandler {
        navController.navigate("VistaPrincipal")
    }

    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primary
                            ),
                        contentAlignment = Alignment.CenterStart,

                    ) {

                        IconButton(onClick = {
                            navController.navigate("VistaPrincipal")
                        },Modifier.padding(start = 30.dp)) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew, // Usa Icons.Default.Menu si prefieres
                                contentDescription = "Regresar",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(30.dp).align(Alignment.CenterStart)
                            )
                        }
                        Text(
                            "Ayuda",
                            fontSize = 26.sp,
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface

                        )

                }
            }

            item {
                Row(Modifier.padding(20.dp)) {
                    Box(Modifier.padding(9.dp)) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Gray)) {
                                    append("menú")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append(" al ser presionado desplegará un conjunto de opciones las cuales son: Modificar colores, Cambiar a modo oscuro/claro, Ayuda y Contacta a Soporte.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Row(Modifier.padding(10.dp)) {
                    Box(Modifier.padding(6.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.color),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(55.dp)
                                .height(55.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("Los botones de colores sirven para cambiar la apariencia de la aplicación. Cada color tiene su modo oscuro y su modo claro.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Row(Modifier.padding(10.dp)) {
                    Box(Modifier.padding(6.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.eclipse),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(55.dp)
                                .height(55.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón para ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Gray)) {
                                    append("Cambiar a modo oscuro/claro ")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("cambia la apariencia de la aplicación con colores más oscuros o más claros.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Row(Modifier.padding(10.dp)) {
                    Box(Modifier.padding(13.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Help,
                            contentDescription = "Ayuda ",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón para ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Gray)) {
                                    append("Ayuda ")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("abre está ventana la cual indica como funciona la aplicación.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }


            item {
                Row(Modifier.padding(20.dp)) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.edit_icon),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(55.dp)
                                .height(55.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = EditBlue)) {
                                    append("Editar")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append(" al ser presionado le da acceso a una pantalla en donde podrá editar el producto correspondiente y actualizarlo en el catálogo. ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                                    append("Importante: ")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("No se podrá editar ningun producto si no están todos los campos llenos (incluyendo fotografía del producto) ni si existe algun valor inválido.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Row(Modifier.padding(20.dp)) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon2),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(55.dp)
                                .height(55.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = DeleteRed)) {
                                    append("Borrar")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append(" al ser presionado le aparecerá una confirmación para asegurar que de verdad quiere borrar el producto del catálogo")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }

            item {
                Row(Modifier.padding(20.dp)) {
                    Box(modifier = Modifier.size(50.dp)) {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawRoundRect(
                                color = GreenAdd,
                                cornerRadius = CornerRadius(22f, 22f)
                            )
                        }
                        Text(
                            text = "+",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = GreenAdd)) {
                                    append("+")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append(" al ser presionado le da acceso a una pantalla en donde podrá agregar un producto nuevo al catálogo. ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                                    append("Importante: ")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("No se podrá agregar ningun producto si no están todos los campos llenos (incluyendo fotografía del producto) ni si existe algun valor inválido.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            item { HorizontalDivider() }


            item {
                Row(Modifier.padding(20.dp)) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.camera_512),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(55.dp)
                                .height(55.dp)
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("El botón ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = EditBlue)) {
                                    append("Foto")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append(" al ser presionado le da acceso a una pantalla en donde podrá tomar foto del producto a agregar. En la pestaña de editar se le puede dar a un botón en la misma posición para cambiar dicha foto. ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                                    append("Importante: ")
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("Solo se registrará la imagen dentro del cuadrado indicado en la interfaz. Es necesario que todos los productos tengan su imágen. La foto NO se guardará en la galeria.")
                                }
                            },
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }


        }
    }
}
