package com.arquitectura.gasolineraapp.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class mConstante(
    var id: Int = 0,
    var nombre: String = "",
    var valor: Double = 0.0,
    var unidad: String = ""
) {

    fun listar(context: Context): List<mConstante> {
        val lista = mutableListOf<mConstante>()
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Constantes", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    mConstante(
                        id = cursor.getInt(0),
                        nombre = cursor.getString(1),
                        valor = cursor.getDouble(2),
                        unidad = cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues()
        valores.put("valor", valor)
        val resultado = db.update("Constantes", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun obtenerValorPorNombre(context: Context, nombreConstante: String): Double {
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor = db.rawQuery("SELECT valor FROM Constantes WHERE nombre = ?", arrayOf(nombreConstante))
        val valor = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        db.close()
        return valor
    }


}
