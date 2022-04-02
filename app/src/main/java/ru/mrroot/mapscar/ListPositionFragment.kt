package ru.mrroot.mapscar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.MarkerOptions
import ru.mrroot.mapscar.databinding.FragmentListPositionBinding

class ListPositionFragment(private var markerList: MutableList<MarkerOptions>) : Fragment(),
    ListMarkerAdapter.AdapterMarkerFunctional {

    companion object {
        fun newInstance(markerListIns: MutableList<MarkerOptions>) =
            ListPositionFragment(markerListIns)
    }

    private lateinit var listPosition: FragmentListPositionBinding
    private lateinit var adapter: ListMarkerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listPosition = FragmentListPositionBinding.inflate(inflater, container, false)
        return listPosition.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonBackOn()
        adapter = ListMarkerAdapter(markerList, this)
        listPosition.mainRecycler.adapter = adapter
    }

    private fun setButtonBackOn() {
        val actionBar = (requireActivity() as MainActivity).getSupportActionBar()
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun removeMarker(marker: MarkerOptions) {
        markerList.remove(marker)
        adapter.notifyDataSetChanged()
    }

    override fun renameTitle(marker: MarkerOptions, text: String) {
        marker.title(text)
    }
}