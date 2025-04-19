package com.arquitectura.gasolineraapp.modelo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) : SQLiteOpenHelper(
    context,
    "GasolineraDB", // nombre de la base de datos
    null,
    2 // versión
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE Sucursal (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                direccion TEXT NOT NULL,
                latitud REAL NOT NULL,
                longitud REAL NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Solo para desarrollo: elimina y recrea la tabla
        db?.execSQL("DROP TABLE IF EXISTS Sucursal")
        onCreate(db)
    }
}
