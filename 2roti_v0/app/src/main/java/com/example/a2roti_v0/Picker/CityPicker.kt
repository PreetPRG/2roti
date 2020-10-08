package com.example.a2roti_v0.Adapter
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.a2roti_v0.Interfaces.OnCityPickerListener
import com.example.a2roti_v0.Model.City

import com.example.a2roti_v0.Picker.CityPickerDialog.CityPickerDialogInteractionListener
import com.example.a2roti_v0.Picker.CityPickerDialog
import com.example.a2roti_v0.R


import java.util.*

class CityPicker : CityPickerDialog.CityPickerDialogInteractionListener {
    // region state
    var context: Context? = null
    private val CITIES = equalCityObject.toTypedArray()
    private var sortBy = SORT_BY_NONE
    private var onCityPickerListener: OnCityPickerListener? = null
    private var canSearch = true
    private var cities: MutableList<City>? = null

    // endregion
    // region Constructors
    private constructor() {}
    internal constructor(builder: Builder) {
        sortBy = builder.sortBy
        if (builder.onCityPickerListener != null) {
            onCityPickerListener = builder.onCityPickerListener
        }
        context = builder.context
        canSearch = builder.canSearch
        cities = ArrayList(Arrays.asList(*CITIES))
        sortCities(cities!!)
    }

    // endregion
    // region Listeners
    override fun sortCities(cities: MutableList<City>) {
        when (sortBy) {
            SORT_BY_NAME -> Collections.sort(cities, Comparator { city1, city2 -> city1.cityName!!.trim { it <= ' ' }.compareTo(city2.cityName!!.trim { it <= ' ' }, ignoreCase = true) })
        }
    }

    override val allCities: MutableList<City>?
        get() = cities

//    override fun sortCities(searchResults: MutableList<City>) {
//
//    }

    override fun canSearch(): Boolean {
        return canSearch
    }

    // endregion
    // region Utility Methods
    // region Utility Methods
    fun showDialog(supportFragmentManager: FragmentManager) {
        if (cities == null || cities!!.isEmpty()) {
            throw IllegalArgumentException(context!!.getString(R.string.error_no_cities_found))
        } else {
            val cityPickerDialog: CityPickerDialog = CityPickerDialog.Companion.newInstance()
            if (onCityPickerListener != null) {
                cityPickerDialog.setCityPickerListener(onCityPickerListener)
                //.setStatePickerListener(onStatePickerListener);
            }
            cityPickerDialog.setDialogInteractionListener(this)
            cityPickerDialog.show(supportFragmentManager, CITY_TAG)
        }
    }

    fun setCities(cities: List<City>) {
        this.cities!!.clear()
        this.cities!!.addAll(cities)
        sortCities((this.cities)!!)
    }

    fun getCityByName(cityNamee: String): City? {
        var cityName = cityNamee
        cityName = cityName.toUpperCase()
        val city = City()
        city.cityName = cityName
        val i = Arrays.binarySearch(CITIES, city, NameComparator())
        return if (i < 0) {
            null
        } else {
            CITIES.get(i)
        }
    }

    // endregion
    // region Builder
    class Builder() {
        var context: Context? = null
        var sortBy = SORT_BY_NONE
        var canSearch = true
        var onCityPickerListener: OnCityPickerListener? = null
        fun with(context: Context): Builder {
            this.context = context
            return this
        }

        fun sortBy(sortBy: Int): Builder {
            this.sortBy = sortBy
            return this
        }

        fun listener(onCityPickerListener: OnCityPickerListener): Builder {
            this.onCityPickerListener = onCityPickerListener
            return this
        }

        fun canSearch(canSearch: Boolean): Builder {
            this.canSearch = canSearch
            return this
        }

        fun build(): CityPicker {
            return CityPicker(this)
        }
    }

    // endregion
    class NameComparator() : Comparator<City> {
        override fun compare(city: City, nextCity: City): Int {
            return city.cityName!!.compareTo(nextCity.cityName!!)
        }
    }

    companion object {
        var equalCityObject: MutableList<City> = ArrayList()

        // region Variables
        val SORT_BY_NONE = 0
        val SORT_BY_NAME = 1
        private val CITY_TAG = "CITY_PICKER"
    }
}