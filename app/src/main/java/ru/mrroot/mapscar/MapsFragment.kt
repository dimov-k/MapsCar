package ru.mrroot.mapscar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import ru.mrroot.mapscar.databinding.FragmentMapsBinding
import ru.mrroot.mapscar.geo.GeoPermission

class MapsFragment : Fragment(){

    private lateinit var mapsBinding: FragmentMapsBinding

    companion object {
        private lateinit var googleMapUtil: GoogleMapUtil
        fun newInstance(): Fragment {
            googleMapUtil = GoogleMapUtil()
            return MapsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setButtonBackOff()
        mapsBinding = FragmentMapsBinding.inflate(inflater, container, false)
        return mapsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(googleMapUtil.callback((requireActivity() as MainActivity)))

        mapsBinding.btnListPosition.setOnClickListener {
            toListPositionFragment()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        if (GeoPermission.isPermissionsResult(requestCode, grantResults)) {
            googleMapUtil.goToMapGeoLocation()
        }
    }

    private fun setButtonBackOff() {
        val actionBar = (requireActivity() as MainActivity).getSupportActionBar()
        actionBar?.setHomeButtonEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun toListPositionFragment() {
        (requireActivity() as MainActivity).navigateTo(
            ListPositionFragment.newInstance(
                googleMapUtil.getMarkerList()
            )
        )
    }
}