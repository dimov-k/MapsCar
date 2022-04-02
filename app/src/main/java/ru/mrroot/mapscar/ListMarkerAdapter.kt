package ru.mrroot.mapscar

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.MarkerOptions
import ru.mrroot.mapscar.databinding.ItemListPositionBinding

class ListMarkerAdapter(
    private val listMarker: MutableList<MarkerOptions>,
    val adapterMarkerFun: AdapterMarkerFunctional
) :
    RecyclerView.Adapter<ListMarkerAdapter.ViewHolder>() {

    override fun getItemCount() = listMarker.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMarker[position])
    }

    inner class ViewHolder(
        parent: ViewGroup,
        private val binding: ItemListPositionBinding = ItemListPositionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(marker: MarkerOptions,) {
            with(binding) {
                titleId.setText(marker.title)
                bodyId.text =
                    " ${marker.position.latitude.toString()} / ${marker.position.longitude.toString()}"
                imageDelete.setOnClickListener { adapterMarkerFun.removeMarker(marker) }
                addTitleListener(titleId,marker)
            }
        }
    }

    private fun addTitleListener(titleId: EditText, marker: MarkerOptions) {
        titleId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(s: Editable?) {
                adapterMarkerFun.renameTitle(marker, titleId.text.toString())
            }
        });
    }

    interface AdapterMarkerFunctional {
        fun removeMarker(marker: MarkerOptions)
        fun renameTitle(marker: MarkerOptions, text: String)
    }
}