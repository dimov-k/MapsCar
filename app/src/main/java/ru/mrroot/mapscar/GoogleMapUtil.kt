package ru.mrroot.mapscar

import android.location.Geocoder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ru.mrroot.mapscar.gcar.GCar
import ru.mrroot.mapscar.geo.GeoData
import ru.mrroot.mapscar.geo.GeoLocation
import ru.mrroot.mapscar.geo.GeoPermission
import ru.mrroot.mapscar.geo.IGeo
import java.io.IOException
import java.lang.Math.toRadians
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class GoogleMapUtil() {

    private lateinit var googleMap: GoogleMap
    private lateinit var currentMarker: Marker
    private lateinit var mainActivity: MainActivity
    private lateinit var gCar : GCar
    private val markers = mutableListOf<MarkerOptions>()

    fun callback(mainActivityin: MainActivity): OnMapReadyCallback {
        mainActivity = mainActivityin
        return OnMapReadyCallback {
            onMapReady(it)
        }
    }

    private fun onMapReady(googleMapIn: GoogleMap) {
        googleMap = googleMapIn

        googleMap.clear()
        setDefaultCamera()
        addSaveMarkers()
        if (GeoPermission().isPemissions(mainActivity)) {
            goToMapGeoLocation()
        }
        gCar = GCar(googleMap,mainActivity)
    }

    private fun addSaveMarkers() {
        markers.forEach { googleMap.addMarker(it) }
    }

    private fun addMarkerOnClickForMap() {
        googleMap.setOnMapLongClickListener { latLng ->
            addMarker(
                MarkerOptions().position(
                    latLng
                )
            )
        }
    }

    private fun setDefaultCamera() {
        val sydney = LatLng(-34.0, 151.0)
        currentMarker = googleMap
            .addMarker(
                MarkerOptions().position(sydney)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)) // Цвет маркера
                    .alpha(0.7f) // прозрачности маркера
                    .visible(false)

            )!!
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun goToMapGeoLocation() {

        GeoLocation().requestLocation(mainActivity.applicationContext, object : IGeo {
            override fun geoDataResult(geoData: GeoData) {
                goTo(geoData)
            }
        })
    }

    fun goTo(geoData: GeoData) {

        val currentPosition = LatLng(geoData.latitude, geoData.longitude)

        currentMarker?.setPosition(currentPosition)
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentPosition,
                12.toFloat()
            )
        )
        gCar.start()
    }

    private fun addMarker(marker: MarkerOptions) {
        markers.add(marker)
        getAddress(marker)
        googleMap.addMarker(
            marker
        )
    }

    private fun getAddress(marker: MarkerOptions) {
        val geocoder = Geocoder(mainActivity.applicationContext)
        Thread {
            try {
                val addresses =
                    geocoder.getFromLocation(marker.position.latitude, marker.position.longitude, 1)
                marker.title(addresses[0].getAddressLine(0))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun getMarkerList(): MutableList<MarkerOptions> {
        return markers
    }
    companion object{

        fun positionToRotation(
            current: LatLng,
            destination: LatLng
        ): Float {

            var currentLat = current.latitude
            var currentLon = current.longitude
            var destinationLat = destination.latitude
            var destinationLon = destination.longitude

            currentLat = toRadians(currentLat)
            currentLon = toRadians(currentLon)
            destinationLat = toRadians(destinationLat)
            destinationLon = toRadians(destinationLon)

            val X: Double = cos(destinationLat) * sin((destinationLon - currentLon))
            val Y: Double = cos(currentLat) * sin(destinationLat) -
                    sin(currentLat) * cos(destinationLat) * cos((destinationLon - currentLon))
            val radianBearing = atan2(X, Y)

            return Math.toDegrees(radianBearing).toFloat()
        }
    }
}