package com.arquitectura.gasolineraapp.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class mSucursalCombustible(
    var id: Int = 0,
    var idSucursal: Int = 0,
    var idCombustible: Int = 0,
    var cantidadBombas: Int = 0,
    var horaMedicion: String = "",
    var combustibleDisponible: Double = 0.0
) {

    fun insertar(context: Context): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues().apply {
            put("idSucursal", idSucursal)
            put("idCombustible", idCombustible)
            put("cantidadBombas", cantidadBombas)
            put("horaMedicion", horaMedicion)
            put("combustibleDisponible", combustibleDisponible)
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
            put("combustibleDisponible", combustibleDisponible)
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
                        cantidadBombas = cursor.getInt(3),
                        horaMedicion = cursor.getString(4) ?: "",
                        combustibleDisponible = cursor.getDouble(5)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun obtenerLitrosDisponibles(context: Context, idSucursalCombustible: Int): Double {
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor = db.rawQuery("SELECT combustibleDisponible FROM SucursalCombustible WHERE id = ?", arrayOf(idSucursalCombustible.toString()))
        val litros = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        db.close()
        return litros
    }

    fun obtenerBombas(context: Context, idSucursalCombustible: Int): Int {
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor = db.rawQuery("SELECT cantidadBombas FROM SucursalCombustible WHERE id = ?", arrayOf(idSucursalCombustible.toString()))
        val bombas = if (cursor.moveToFirst()) cursor.getInt(0) else 1
        cursor.close()
        db.close()
        return bombas
    }
}
