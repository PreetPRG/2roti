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
import com.example.a2roti_v0.Adapter.CityAdapter
import com.example.a2roti_v0.Interfaces.OnCityPickerListener
import com.example.a2roti_v0.Interfaces.OnItemClickCityListener
import com.example.a2roti_v0.Model.City
import com.example.a2roti_v0.R
import java.util.*

class CityPickerDialog constructor() : DialogFragment(), OnItemClickCityListener {
    // region Variables
    private var dialogInteractionListener: CityPickerDialogInteractionListener? = null
    private var searchEditText: EditText? = null
    private var cityRecyclerView: RecyclerView? = null
    private var adapter: CityAdapter? = null
    private var searchResults: MutableList<City>? = null
    private var listener: OnCityPickerListener? = null

    // endregion
    // region Lifecycle
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.city_picker, null)
        getDialog()!!.setTitle(R.string.country_picker_header)
        searchEditText = view.findViewById(R.id.city_picker_search)
        cityRecyclerView = view.findViewById(R.id.city_recycler_view)
        setupRecyclerView()
        if (!dialogInteractionListener!!.canSearch()) {
            searchEditText!!.visibility = View.GONE
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

    public override fun onItemClickCityListener(city: City?) {
        if (listener != null) {
            listener!!.onSelectCity(city)
        }
        dismiss()
    }

    // endregion
    // region Setter Methods
    fun setCityPickerListener(listener: OnCityPickerListener?) {
        this.listener = listener
    }

    fun setDialogInteractionListener(
            dialogInteractionListener: CityPickerDialogInteractionListener?) {
        this.dialogInteractionListener = dialogInteractionListener
    }

    // endregion
    // region Utility Methods
    private fun search(searchQuery: String) {
        searchResults!!.clear()
        for (city: City in dialogInteractionListener!!.allCities!!) {
            if (city.cityName!!.toLowerCase(Locale.ENGLISH).contains(searchQuery.toLowerCase())) {
                searchResults!!.add(city)
            }
        }
        dialogInteractionListener!!.sortCities((searchResults)!!)
        adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        searchResults = ArrayList()
        searchResults!!.addAll((dialogInteractionListener!!.allCities)!!)
        adapter = CityAdapter((getActivity())!!, searchResults!!, this)
        cityRecyclerView!!.setHasFixedSize(true)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(getContext())
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        cityRecyclerView!!.setLayoutManager(layoutManager)
        cityRecyclerView!!.setAdapter(adapter)
    }

    // endregion
    //region Interface
    interface CityPickerDialogInteractionListener {
        val allCities: MutableList<City>?
        fun sortCities(searchResults: MutableList<City>)
        fun canSearch(): Boolean
    } // endregion

    companion object {
        // endregion
        // region Constructors
        fun newInstance(): CityPickerDialog {
            return CityPickerDialog()
        }
    }
}