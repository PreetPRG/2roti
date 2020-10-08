package com.example.a2roti_v0.Picker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a2roti_v0.Adapter.CountriesAdapter
import com.example.a2roti_v0.Interfaces.OnCountryPickerListener
import com.example.a2roti_v0.Interfaces.OnItemClickListener
import com.example.a2roti_v0.Model.Country
import com.example.a2roti_v0.R
import java.util.*

class CountryPickerDialog constructor() : DialogFragment(), OnItemClickListener {
    // region Variables
    private var dialogInteractionListener: CountryPickerDialogInteractionListener? = null
    private var searchEditText: EditText? = null
    private var countriesRecyclerView: RecyclerView? = null
    private var adapter: CountriesAdapter? = null
    private var searchResults: MutableList<Country>? = null
    private var listener: OnCountryPickerListener? = null

    // endregion
    // region Lifecycle
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.country_picker, null)
        getDialog()!!.setTitle(R.string.country_picker_header)
        searchEditText = view.findViewById(R.id.country_code_picker_search)
        countriesRecyclerView = view.findViewById(R.id.countries_recycler_view)
        setupRecyclerView()
        if (!dialogInteractionListener!!.canSearch()) {
            searchEditText!!.setVisibility(View.GONE)
        }
        searchEditText!!.addTextChangedListener(object : TextWatcher {
            public override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            public override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            public override fun afterTextChanged(searchQuery: Editable) {
                search(searchQuery.toString())
            }
        })
        return view
    }

    public override fun onStart() {
        super.onStart()
        val params: ViewGroup.LayoutParams = getDialog()!!.getWindow()!!.getAttributes()
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.MATCH_PARENT
        getDialog()!!.getWindow()!!.setAttributes(params as WindowManager.LayoutParams?)
    }

    public override fun onItemClicked(country: Country?) {
        if (listener != null) {
            listener!!.onSelectCountry(country)
        }
        dismiss()
    }

    // endregion
    // region Setter Methods
    fun setCountryPickerListener(listener: OnCountryPickerListener?) {
        this.listener = listener
    }

    fun setDialogInteractionListener(
            dialogInteractionListener: CountryPickerDialogInteractionListener?) {
        this.dialogInteractionListener = dialogInteractionListener
    }

    // endregion
    // region Utility Methods
    private fun search(searchQuery: String) {
        searchResults!!.clear()
        for (country: Country in dialogInteractionListener!!.allCountries!!) {
            if (country.name!!.toLowerCase(Locale.ENGLISH).contains(searchQuery.toLowerCase())) {
                searchResults!!.add(country)
            }
        }
        dialogInteractionListener!!.sortCountries((searchResults)!!)
        adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        searchResults = ArrayList()
        searchResults!!.addAll((dialogInteractionListener!!.allCountries)!!)
        adapter = CountriesAdapter(getActivity()!!, searchResults!!, this)
        countriesRecyclerView!!.setHasFixedSize(true)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(getContext())
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        countriesRecyclerView!!.setLayoutManager(layoutManager)
        countriesRecyclerView!!.setAdapter(adapter)
    }

    // endregion
    //region Interface
    interface CountryPickerDialogInteractionListener {
        val allCountries: MutableList<Country>?
        fun sortCountries(searchResults: MutableList<Country>)
        fun canSearch(): Boolean
    } // endregion

    companion object {
        // endregion
        // region Constructors
        fun newInstance(): CountryPickerDialog {
            return CountryPickerDialog()
        }
    }
}