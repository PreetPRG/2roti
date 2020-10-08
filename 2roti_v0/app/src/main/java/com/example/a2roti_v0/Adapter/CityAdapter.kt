package com.example.a2roti_v0.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a2roti_v0.Interfaces.OnItemClickCityListener
import com.example.a2roti_v0.Model.City
import com.example.a2roti_v0.R

class CityAdapter // endregion
//region Constructor
(private val context: Context, private val cities: List<City>,
        // region Variables
 private val listener: OnItemClickCityListener
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    // endregion
    // region Adapter Methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        holder.cityName.text = city.cityName
        holder.rootView.setOnClickListener { listener.onItemClickCityListener(city) }
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    // endregion
    // region ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val cityName: TextView
        internal val rootView: LinearLayout

        init {
            cityName = itemView.findViewById(R.id.city_title)
            rootView = itemView.findViewById(R.id.rootCityView)
        }
    } // endregion

}