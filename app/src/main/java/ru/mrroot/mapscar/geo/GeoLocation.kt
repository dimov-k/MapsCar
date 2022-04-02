package ru.mrroot.mapscar.geo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

class GeoLocation {

    fun requestLocation(context: Context, geo: IGeo) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE

        val provider = locationManager.getBestProvider(criteria, true)
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 2000, 10f, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    geo.geoDataResult(
                        GeoData(
                            location.latitude,   //Широта
                            location.longitude,  //Долгота
                            location.accuracy    //Точность
                        )
                    )
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            })
        }
    }
}