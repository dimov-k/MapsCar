package ru.mrroot.mapscar.gcar

import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import ru.mrroot.mapscar.GoogleMapUtil.Companion.positionToRotation
import ru.mrroot.mapscar.MainActivity
import ru.mrroot.mapscar.R

class GCar(private val googleMap: GoogleMap, private val mainActivity: MainActivity) :
    IMoveCar, GoogleMap.OnMarkerClickListener {

    private lateinit var markerCar: Marker
    private lateinit var positionCar: LatLng
    private lateinit var destination: Marker
    private lateinit var car: Car
    private lateinit var trackList: ArrayList<LatLng>
    private var rotation: Float = 0.0F

    fun start() {
        car = Car(mainActivity, this)
        googleMap.setOnMarkerClickListener(this)
        setCarToMap()

    }

    private fun setDestination() {
        var bias = 0.04
        val destinationPosition = LatLng(
            googleMap.cameraPosition.target.latitude - bias,
            googleMap.cameraPosition.target.longitude + bias
        )

        destination = googleMap
            .addMarker(
                MarkerOptions().position(destinationPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)) // Цвет маркера
                    .alpha(0.7f) // прозрачности маркера

            )!!
    }

    private fun setCarToMap() {

        val carPosition = LatLng(
            googleMap.cameraPosition.target.latitude + 0.04,
            googleMap.cameraPosition.target.longitude - 0.04
        )

        markerCar = googleMap
            .addMarker(
                MarkerOptions()
                    .position(carPosition)
                    .icon(BitmapDescriptorFactory.fromBitmap(car.getImageCar()))
                    .draggable(false)
                    .anchor(0.5f, 0.5f)
                    .rotation(90.0f)


            )!!
        positionCar = markerCar.position
    }

    fun createTrack() {

        val polylineOptions = PolylineOptions()
            .color(R.color.track)

        trackList = Track().getTrack(positionCar, destination.position)
        polylineOptions.addAll(trackList)
        setRotationCar(trackList[0], trackList[1])

        val polyline: Polyline = googleMap.addPolyline(polylineOptions)

    }


    private fun setRotationCar(start: LatLng, stop: LatLng) {
        rotation = positionToRotation(start, stop)
        markerCar.rotation = rotation
    }

    private var drive: Double = 0.0
    private var trackNum = 0


    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker == markerCar) {
            if (!car.move) {
                setDestination()
                createTrack()
                car.startDrive()
            }
            return true
        }
        return false
    }

    override fun drive() {

        drive += 0.001

        val x: Double
        val y: Double
        val x1: Double
        val y1: Double
        val x2: Double
        val y2: Double
        val yy: Double

        if (trackList[trackNum].longitude < trackList[trackNum + 1].longitude) {
            x1 = trackList[trackNum].longitude
            y1 = trackList[trackNum].latitude
            x2 = trackList[trackNum + 1].longitude
            y2 = trackList[trackNum + 1].latitude
            y = y1 - drive
            yy = y2
        } else {
            x1 = trackList[trackNum + 1].longitude
            y1 = trackList[trackNum + 1].latitude
            x2 = trackList[trackNum].longitude
            y2 = trackList[trackNum].latitude
            y = y2 - drive
            yy = y1
        }

        x = ((y - y1) / (y2 - y1) * (x2 - x1)) + x1

        if (y <= yy) {

            trackNum++
            if (trackNum >= trackList.size - 1) {
                car.stopDrive()
                trackNum = 0
                return
            }

            drive = 0.0
            setRotationCar(trackList[trackNum], trackList[trackNum + 1])
        }

        val currentPosition = LatLng(y, x)
        markerCar.position = currentPosition
    }

    override fun stop() {
        Toast.makeText(mainActivity.applicationContext, R.string.stop_car, Toast.LENGTH_LONG).show()
    }
}

