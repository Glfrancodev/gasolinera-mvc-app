package com.arquitectura.gasolineraapp.vista.sucursal

import android.app.Activity
import android.widget.Button
import com.arquitectura.gasolineraapp.R
import com.google.android.gms.maps.MapView

class vMapa(activity: Activity) {
    val mapView: MapView = activity.findViewById(R.id.mapView)
    val btnConfirmar: Button = activity.findViewById(R.id.btnConfirmar)
}
