package com.example.a2roti_v0.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a2roti_v0.Interfaces.OnItemClickListener
import com.example.a2roti_v0.Model.Country
import com.example.a2roti_v0.R

class CountriesAdapter // endregion
//region Constructor
(private val context: Context, private val countries: List<Country>,
        // region Variables
 private val listener: OnItemClickListener
) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    // endregion
    // region Adapter Methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.countryNameText.text = country.name
        country.loadFlagByCode(context)
        if (country.flag != -1) {
            holder.countryFlagImageView.setImageResource(country.flag)
        }
        holder.rootView.setOnClickListener { listener.onItemClicked(country) }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    // endregion
    // region ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryFlagImageView: ImageView
        val countryNameText: TextView
        val rootView: LinearLayout

        init {
            countryFlagImageView = itemView.findViewById(R.id.country_flag)
            countryNameText = itemView.findViewById(R.id.country_title)
            rootView = itemView.findViewById(R.id.rootView)
        }
    } // endregion

}