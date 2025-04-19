package com.arquitectura.gasolineraapp.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class mSucursal(
    var id: Int = 0,
    var nombre: String = "",
    var direccion: String = "",
    var latitud: Double = 0.0,
    var longitud: Double = 0.0
) {

    fun insertar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("direccion", direccion)
        valores.put("latitud", latitud)
        valores.put("longitud", longitud)

        val resultado = db.insert("Sucursal", null, valores)
        db.close()
        return resultado != -1L
    }

    fun actualizar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("direccion", direccion)
        valores.put("latitud", latitud)
        valores.put("longitud", longitud)

        val resultado = db.update("Sucursal", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val resultado = db.delete("Sucursal", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun listar(context: Context): List<mSucursal> {
        val lista = mutableListOf<mSucursal>()
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Sucursal", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    mSucursal(
                        id = cursor.getInt(0),
                        nombre = cursor.getString(1),
                        direccion = cursor.getString(2),
                        latitud = cursor.getDouble(3),
                        longitud = cursor.getDouble(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
