package com.example.a2roti_v0.Picker

import android.content.Context
import android.telephony.TelephonyManager
import androidx.fragment.app.FragmentManager
import com.example.a2roti_v0.Interfaces.OnCountryPickerListener
import com.example.a2roti_v0.Model.Country
import com.example.a2roti_v0.R
import java.util.*

class CountryPicker : CountryPickerDialog.CountryPickerDialogInteractionListener {
    private var context: Context? = null
    private var sortBy: Int = SORT_BY_NONE
    private var onCountryPickerListener: OnCountryPickerListener? = null
    private var canSearch: Boolean = true
    private var countries: MutableList<Country>? = null

    // endregion
    // region Constructors
    private constructor() {}
    internal constructor(builder: Builder) {
        sortBy = builder.sortBy
        if (builder.onCountryPickerListener != null) {
            onCountryPickerListener = builder.onCountryPickerListener
        }
        context = builder.context
        canSearch = builder.canSearch
        countries = ArrayList(Arrays.asList(*COUNTRIES))
        sortCountries(countries!!)
    }

    // endregion
    // region Listeners
    public override fun sortCountries(countries: MutableList<Country>) {
        when (sortBy) {
            SORT_BY_NAME -> {
                Collections.sort(countries, object : Comparator<Country> {
                    public override fun compare(country1: Country, country2: Country): Int {
                        return country1.name!!.trim({ it <= ' ' }).compareTo(country2.name!!.trim({ it <= ' ' }), ignoreCase = true)
                    }
                })
                Collections.sort(countries, object : Comparator<Country> {
                    public override fun compare(country1: Country, country2: Country): Int {
                        return country1.getCode()!!.trim({ it <= ' ' }).compareTo(country2.getCode()!!.trim({ it <= ' ' }), ignoreCase = true)
                    }
                })
                Collections.sort(countries, object : Comparator<Country> {
                    public override fun compare(country1: Country, country2: Country): Int {
                        return country1.dialCode!!.trim({ it <= ' ' }).compareTo(country2.dialCode!!.trim({ it <= ' ' }), ignoreCase = true)
                    }
                })
            }
            SORT_BY_ISO -> {
                Collections.sort(countries, object : Comparator<Country> {
                    public override fun compare(country1: Country, country2: Country): Int {
                        return country1.getCode()!!.trim({ it <= ' ' }).compareTo(country2.getCode()!!.trim({ it <= ' ' }), ignoreCase = true)
                    }
                })
                Collections.sort(countries, object : Comparator<Country> {
                    public override fun compare(country1: Country, country2: Country): Int {
                        return country1.dialCode!!.trim({ it <= ' ' }).compareTo(country2.dialCode!!.trim({ it <= ' ' }), ignoreCase = true)
                    }
                })
            }
            SORT_BY_DIAL_CODE -> Collections.sort(countries, object : Comparator<Country> {
                public override fun compare(country1: Country, country2: Country): Int {
                    return country1.dialCode!!.trim({ it <= ' ' }).compareTo(country2.dialCode!!.trim({ it <= ' ' }), ignoreCase = true)
                }
            })
        }
    }

    override val allCountries: MutableList<Country>?
        get() {
            return countries
        }

    public override fun canSearch(): Boolean {
        return canSearch
    }

    // endregion
    // region Utility Methods
    fun showDialog(supportFragmentManager: FragmentManager) {
        if (countries == null || countries!!.isEmpty()) {
            throw IllegalArgumentException(context!!.getString(R.string.error_no_countries_found))
        } else {
            val countryPickerDialog: CountryPickerDialog = CountryPickerDialog.Companion.newInstance()
            if (onCountryPickerListener != null) {
                countryPickerDialog.setCountryPickerListener(onCountryPickerListener)
            }
            countryPickerDialog.setDialogInteractionListener(this)
            countryPickerDialog.show(supportFragmentManager, COUNTRY_TAG)
        }
    }

    fun setCountries(countries: List<Country>) {
        this.countries!!.clear()
        this.countries!!.addAll(countries)
        sortCountries((this.countries)!!)
    }

    val countryFromSIM: Country?
        get() {
            val telephonyManager: TelephonyManager? = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            if ((telephonyManager != null
                            && telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT)) {
                return getCountryByISO(telephonyManager.getSimCountryIso())
            }
            return null
        }

    fun getCountryByLocale(locale: Locale): Country? {
        val countryIsoCode: String = locale.getISO3Country().substring(0, 2).toLowerCase()
        return getCountryByISO(countryIsoCode)
    }

    fun getCountryByName(countryNamee: String): Country? {
        var countryName: String = countryNamee
        countryName = countryName.toUpperCase()
        val country: Country = Country()
        country.name=countryName
        val i: Int = Arrays.binarySearch(COUNTRIES, country, NameComparator())
        if (i < 0) {
            return null
        } else {
            return COUNTRIES.get(i)
        }
    }

    fun getCountryByISO(countryIsoCodee: String): Country? {
        var countryIsoCode: String = countryIsoCodee
        countryIsoCode = countryIsoCode.toUpperCase()
        val country: Country = Country()
        country.setCode(countryIsoCode)
        val i: Int = Arrays.binarySearch(COUNTRIES, country, ISOCodeComparator())
        if (i < 0) {
            return null
        } else {
            return COUNTRIES.get(i)
        }
    }

    // endregion
    // region Builder
    class Builder constructor() {
        var context: Context? = null
        var sortBy: Int = SORT_BY_NONE
        var canSearch: Boolean = true
        var onCountryPickerListener: OnCountryPickerListener? = null
        fun with(context: Context): Builder {
            this.context = context
            return this
        }

        fun sortBy(sortBy: Int): Builder {
            this.sortBy = sortBy
            return this
        }

        fun listener(onCountryPickerListener: OnCountryPickerListener): Builder {
            this.onCountryPickerListener = onCountryPickerListener
            return this
        }

        fun canSearch(canSearch: Boolean): Builder {
            this.canSearch = canSearch
            return this
        }

        fun build(): CountryPicker {
            return CountryPicker(this)
        }
    }

    // endregion
    // region Comparators
    class ISOCodeComparator constructor() : Comparator<Country> {
        public override fun compare(country: Country, nextCountry: Country): Int {
            return country.getCode()!!.compareTo(nextCountry.getCode()!!)
        }
    }

    class NameComparator constructor() : Comparator<Country> {
        public override fun compare(country: Country, nextCountry: Country): Int {
            return country.name!!.compareTo(nextCountry.name!!)
        }
    } // endregion

    companion object {
        // region Countries
        val COUNTRIES: Array<Country> = arrayOf(
                Country(101, "IN", "India", "+91", R.drawable.flag_in, "INR"))

        // endregion
        // region Variables
        val SORT_BY_NONE: Int = 0
        val SORT_BY_NAME: Int = 1
        val SORT_BY_ISO: Int = 2
        val SORT_BY_DIAL_CODE: Int = 3
        private val COUNTRY_TAG: String = "COUNTRY_PICKER"
    }
}