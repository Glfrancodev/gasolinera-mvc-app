package com.arquitectura.gasolineraapp.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class mTipoCombustible(
    var id: Int = 0,
    var nombre: String = "",
    var descripcion: String = ""
) {

    fun insertar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("descripcion", descripcion)
        val resultado = db.insert("TipoCombustible", null, valores)
        db.close()
        return resultado != -1L
    }

    fun actualizar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("descripcion", descripcion)
        val resultado = db.update("TipoCombustible", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val resultado = db.delete("TipoCombustible", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun listar(context: Context): List<mTipoCombustible> {
        val lista = mutableListOf<mTipoCombustible>()
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM TipoCombustible", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    mTipoCombustible(
                        id = cursor.getInt(0),
                        nombre = cursor.getString(1),
                        descripcion = cursor.getString(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
