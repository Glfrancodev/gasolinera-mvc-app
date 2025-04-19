package com.arquitectura.gasolineraapp.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class mSucursalCombustible(
    var id: Int = 0,
    var idSucursal: Int = 0,
    var idCombustible: Int = 0,
    var cantidadBombas: Int = 0
) {

    fun insertar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues().apply {
            put("idSucursal", idSucursal)
            put("idCombustible", idCombustible)
            put("cantidadBombas", cantidadBombas)
        }
        val resultado = db.insert("SucursalCombustible", null, valores)
        db.close()
        return resultado != -1L
    }

    fun actualizar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues().apply {
            put("idSucursal", idSucursal)
            put("idCombustible", idCombustible)
            put("cantidadBombas", cantidadBombas)
        }
        val resultado = db.update("SucursalCombustible", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val resultado = db.delete("SucursalCombustible", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun listar(context: Context): List<mSucursalCombustible> {
        val lista = mutableListOf<mSucursalCombustible>()
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM SucursalCombustible", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    mSucursalCombustible(
                        id = cursor.getInt(0),
                        idSucursal = cursor.getInt(1),
                        idCombustible = cursor.getInt(2),
                        cantidadBombas = cursor.getInt(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
