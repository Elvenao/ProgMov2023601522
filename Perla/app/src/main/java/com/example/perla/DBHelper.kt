package com.example.perla


import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.perla.img1

class DBHelper(context: Context) : SQLiteOpenHelper(context, "mibase.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val query1 = "CREATE TABLE IF NOT EXISTS producto(id_producto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, precio DOUBLE NOT NULL, descripcion TEXT, imagen TEXT);"
        db.execSQL(query1)
        val query2 = "INSERT INTO producto(nombre, precio, descripcion, imagen) VALUES " +
                "('Taco Ubuntu', 35.0, 'Taco de bistec con frijoles. Sencillo, útil y listo para usarse sin configuración extra.','$TacoUbuntu')," +
                "('Taco Fedora', 40.0, 'Pollo fancy con ingredientes beta. Rico, pero se rompe con cada actualización..','$TacoFedora')," +
                "('Taco Kali', 50.0, 'Taco especializado en picante y ciberseguridad. No es comida, es un vector de ataque.','$TacoKali')," +
                "('Quesadilla Shell Script', 55.0, 'Queso fundido justo a tiempo... si el script no falla. Con carne al pastor, claro.','$Quesadilla')," +
                "('Burrito Root Access', 85.0, 'Burrito de arrachera con arroz y frijoles. Solo usuarios con permisos pueden terminarlo.','$Burrito')," +
                "('GRUB Burrito', 75.0, 'Burrito de bistec que tarda un poco en arrancar, pero cuando lo hace, es todo lo que necesitas.','$GrubBurrito')," +
                "('Tostada Arch Linux', 30.0, 'Tostada base sin toppings. Tú eliges qué ponerle y cómo romperla en la primera mordida.','$Tostada')," +
                "('apt-get nopalesExtra', 45.0, 'Nopales frescos, sin errores. Si no te gustan, intenta compilar tu versión local.','$nopalesExtra')," +
                "('Salsa SUDO', 15.0, 'Solo para admins: picante nivel crítico. Si no eres root, mejor ni la mires.','$Salsa')," +
                "('Guacamole chmod 777', 40.0, '¡Acceso total al sabor! Cualquiera puede meter mano, incluso ese que nunca lava sus platos.','$Guacamole');"







        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO: Actualizar app
    }

    // Utilizamos el uso del contexto ya proporcionado en la clase
    fun obtenerNombres(): List<Array<Any>> {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM producto", null)
        val lista = mutableListOf<Array<Any>>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val nombre = cursor.getString(1)
            val precio = cursor.getDouble(2)
            val descripcion = cursor.getString(3)
            val imagen = cursor.getString(4)
            lista.add(arrayOf(id, nombre, precio, descripcion, imagen))
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerNombre(id: String): Array<Any>? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM producto WHERE id_producto = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {

            val nombre = cursor.getString(1)
            val precio = cursor.getDouble(2)
            val descripcion = cursor.getString(3)
            val imagen = cursor.getString(4)


            val producto: Array<Any> = arrayOf(nombre, precio, descripcion, imagen)

            cursor.close()
            db.close()
            producto
        } else {
            cursor.close()
            db.close()
            null  // Si no hay registro, retornamos null
        }
    }



}