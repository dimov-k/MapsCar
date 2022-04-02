package ru.mrroot.mapscar

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = MapsFragment.newInstance()
        navigateTo(fragment)
    }

    fun navigateTo(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .replace(R.id.container, fragment)
            .addToBackStack("maps")
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                fragment.parentFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}