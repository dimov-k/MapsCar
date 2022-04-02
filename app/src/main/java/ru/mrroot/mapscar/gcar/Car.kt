package ru.mrroot.mapscar.gcar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mrroot.mapscar.MainActivity
import ru.mrroot.mapscar.R

class Car(private var mainActivity: MainActivity, private var moveCare: IMoveCar) {


    var move = false
    private var speed: Long = 1000

    fun startDrive(){
        move = true
        start()
    }

    fun stopDrive(){
        move = false
        moveCare.stop()
    }

    fun getImageCar(): Bitmap {
        val SIZE_IMG_CAR = 2

        val options = BitmapFactory.Options()
        options.inSampleSize = SIZE_IMG_CAR
        return BitmapFactory.decodeResource(mainActivity.resources, R.drawable.map_car, options)
    }

    private fun start(){
        GlobalScope.launch(context = Dispatchers.Main) {
            while (move){
                moveCare.drive()
                delay(speed)
                println("++")
            }

        }

    }
}