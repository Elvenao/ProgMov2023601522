package com.example.perla

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "mibase.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val query1 = """
            CREATE TABLE IF NOT EXISTS producto(
                id_producto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                precio DOUBLE NOT NULL,
                descripcion TEXT,
                imagen TEXT,
                status INTEGER NOT NULL DEFAULT 1
            );
        """.trimIndent()
        db.execSQL(query1)

        val query2 = """
            INSERT INTO producto(nombre, precio, descripcion, imagen, status) VALUES 
            ('Taco Ubuntu', 35.0, 'Taco de bistec con frijoles. Sencillo, útil y listo para usarse sin configuración extra.','$TacoUbuntu','1'),
            ('Taco Fedora', 40.0, 'Pollo fancy con ingredientes beta. Rico, pero se rompe con cada actualización..','$TacoFedora','1'),
            ('Taco Kali', 50.0, 'Taco especializado en picante y ciberseguridad. No es comida, es un vector de ataque.','$TacoKali','1'),
            ('Quesadilla Shell Script', 55.0, 'Queso fundido justo a tiempo... si el script no falla. Con carne al pastor, claro.','$Quesadilla','1'),
            ('Burrito Root Access', 85.0, 'Burrito de arrachera con arroz y frijoles. Solo usuarios con permisos pueden terminarlo.','$Burrito','1'),
            ('GRUB Burrito', 75.0, 'Burrito de bistec que tarda un poco en arrancar, pero cuando lo hace, es todo lo que necesitas.','$GrubBurrito','1'),
            ('Tostada Arch Linux', 30.0, 'Tostada base sin toppings. Tú eliges qué ponerle y cómo romperla en la primera mordida.','$Tostada','1'),
            ('apt-get nopalesExtra', 45.0, 'Nopales frescos, sin errores. Si no te gustan, intenta compilar tu versión local.','$nopalesExtra','1'),
            ('Salsa SUDO', 15.0, 'Solo para admins: picante nivel crítico. Si no eres root, mejor ni la mires.','$Salsa','1'),
            ('Guacamole chmod 777', 40.0, '¡Acceso total al sabor! Cualquiera puede meter mano, incluso ese que nunca lava sus platos.','$Guacamole','1');
        """.trimIndent()
        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Actualizar
    }

    fun obtenerNombres(): List<Array<Any>> {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT id_producto, nombre, precio, descripcion, imagen FROM producto WHERE status=1", null)
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
        val cursor: Cursor = db.rawQuery(
            "SELECT nombre, precio, descripcion, imagen FROM producto WHERE id_producto = ? AND status=1",
            arrayOf(id)
        )

        return if (cursor.moveToFirst()) {
            val nombre = cursor.getString(0)
            val precio = cursor.getDouble(1)
            val descripcion = cursor.getString(2)
            val imagen = cursor.getString(3)

            val producto: Array<Any> = arrayOf(nombre, precio, descripcion, imagen)

            cursor.close()
            db.close()
            producto
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun insertarProducto(parametros: Array<String>, campos: Array<String>) {
        val db = this.readableDatabase
        var query = "INSERT INTO producto("
        for (i in campos.indices) {
            query += campos[i]
            if (i < campos.size - 1) query += ", "
        }
        query += ") VALUES("
        for (j in parametros.indices) {
            query += "?"
            if (j < parametros.size - 1) query += ", "
        }
        query += ");"

        val statement = db.compileStatement(query)
        for (i in parametros.indices) {
            statement.bindString(i + 1, parametros[i])
        }

        statement.executeInsert()
    }

    fun actualizarProducto(tabla: String, campos: Array<String>, parametros: Array<String>) {
        val db = this.writableDatabase

        // Crear la parte del SET correctamente: "col1 = ?, col2 = ?, ..."
        val setPart = campos.joinToString(", ") { "$it = ?" }

        Log.e("SETPART",setPart)

        // Construir el query completo
        val query = "UPDATE $tabla SET $setPart WHERE id_producto = ?;"

        val statement = db.compileStatement(query)

        // Bind de los valores (sin incluir el ID aún)
        for (i in campos.indices) {
            statement.bindString(i + 1, parametros[i])
        }

        // Último parámetro debe ser el ID
        statement.bindString(campos.size + 1, parametros.last()) // id al final

        statement.executeUpdateDelete()
        db.close()
    }

    fun borrarProducto(idProducto: Int) {
        val db = this.writableDatabase
        val query = "UPDATE producto SET status = 0 WHERE id_producto = ?"
        val statement = db.compileStatement(query)
        statement.bindLong(1, idProducto.toLong())
        statement.executeUpdateDelete()
        db.close()
    }

}
