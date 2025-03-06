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
    val contexto = LocalContext.current
    var nombre by remember {mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxHeight() //En lugar de esto se puede poner fillMaxSize()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,  // Centramos horizontalmente
        verticalArrangement = Arrangement.Center  // Centramos verticalmente
    ) {
        Text(
            modifier = Modifier.clickable(
                onClick = {
                    Toast.makeText(contexto, "Secreto!", Toast.LENGTH_SHORT).show()
            }),
            text = "Nombre:"
        )
        Spacer(modifier = Modifier.height(8.dp))  // Espacio entre elementos

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Introduce tu nombre") }
        )
        Spacer(modifier = Modifier.height(16.dp))  // Más espacio

        Button(
            onClick = {
                Toast.makeText(contexto, "Hola $nombre!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text("Saludar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Previsualizacion() {
    UIPrincipal()
}