import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun main(){
    var seleccion = 0
    val scan = Scanner(System.`in`)
    while(seleccion != 4){
        println("1.-Suma de 3 numeros")
        println("2.-Ingresar nombre completo")
        println("3.-Edad exacta")
        println("4.-Salir")
        print("Escribe: ")
        seleccion = scan.nextInt()
        when(seleccion){
            1 -> threeNumbers()
            2 -> nombre()
            3 -> userBirthDay()
        }
    }    
}

fun nombre(){
    val scan = Scanner(System.`in`)
    print("Escribe tu nombre completo: ")
    val nombre = scan.nextLine()
    println("Tu nombre es $nombre")
}

fun threeNumbers(){
    val scan = Scanner(System.`in`)
    print("Escribe 3 numeros separados por '-': ")
    val numeroString = scan.nextLine()
    val numeroArray = numeroString.split("-")
    var array = IntArray(3)
    for(i in 0..2){
        array[i] = numeroArray[i].toInt()
    }
    val resultado = array[0] + array[1] + array[2]
    println("La suma es $resultado")
}

fun userBirthDay(){
    val scan = Scanner(System.`in`)
    print("Escribe tu fecha de nacimiento en formato yyyy-MM-dd: ")
    val birthDayA = scan.nextLine()
    val fechaHoraActual = LocalDateTime.now()
    var formato = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
    val fechaFormateada = fechaHoraActual.format(formato)
    val currentDate = fechaFormateada.split("-")
    val birthDate = birthDayA.split("-")

    var year = currentDate[0].toInt()
    var month = currentDate[1].toInt()
    var day = currentDate[2].toInt()
    val hour = currentDate[3].toInt()
    val minute = currentDate[4].toInt()
    val second = currentDate[5].toInt()
    
    val birthYear = birthDate[0].toInt()
    val birthMonth = birthDate[1].toInt()
    val birthDay = birthDate[2].toInt()

    var diferenceDay = day - birthDay
    if(diferenceDay < 0){
        diferenceDay = 31 + diferenceDay
        month--
    }
    var diferenceMonth = month - birthMonth
    if(diferenceMonth < 0){
        diferenceMonth = 12 + diferenceMonth
        year--
    }
    var diferenceYear = year - birthYear

    println("Año: $diferenceYear - Mes: $diferenceMonth - Dia: $diferenceDay - Horas: $hour - Minutos: $minute - Segundos: $second")
}           