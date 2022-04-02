package ru.mrroot.mapscar.gcar

import com.google.android.gms.maps.model.LatLng

class Track {
    fun getTrack(positionCar: LatLng, destination: LatLng): ArrayList<LatLng> {
        var trackList = ArrayList<LatLng>()
        trackList.add(positionCar)
        trackList.add(
            LatLng(
                positionCar.latitude - 0.005,
                positionCar.longitude + 0.05
            )
        )

        trackList.add(
            LatLng(
                positionCar.latitude - 0.02,
                positionCar.longitude + 0.01
            )
        )

        trackList.add(
            LatLng(
                positionCar.latitude - 0.04,
                positionCar.longitude + 0.01
            )
        )

        trackList.add(
            LatLng(
                positionCar.latitude - 0.06,
                positionCar.longitude + 0.09
            )
        )

        trackList.add(destination)



        return trackList
    }
}