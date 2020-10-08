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
import com.example.a2roti_v0.Adapter.StateAdapter
import com.example.a2roti_v0.Interfaces.OnItemClickStateListener
import com.example.a2roti_v0.Interfaces.OnStatePickerListener
import com.example.a2roti_v0.Model.State
import com.example.a2roti_v0.R
import java.util.*

class StatePickerDialog constructor() : DialogFragment(), OnItemClickStateListener {
    // region Variables
    private var dialogInteractionListener: StatePickerDialogInteractionListener? = null
    private var searchEditText: EditText? = null
    private var statesRecyclerView: RecyclerView? = null
    private var adapter: StateAdapter? = null
    private var searchResults: MutableList<State>? = null
    private var listener: OnStatePickerListener? = null

    // endregion
    // region Lifecycle
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.state_picker, null)
        getDialog()!!.setTitle(R.string.country_picker_header)
        searchEditText = view.findViewById(R.id.state_picker_search)
        statesRecyclerView = view.findViewById(R.id.state_recycler_view)
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

    public override fun onItemClickStateListener(state: State?) {
        if (listener != null) {
            listener!!.onSelectState(state)
        }
        dismiss()
    }

    // endregion
    // region Setter Methods
    fun setStatePickerListener(listener: OnStatePickerListener?) {
        this.listener = listener
    }

    fun setDialogInteractionListener(
            dialogInteractionListener: StatePickerDialogInteractionListener?) {
        this.dialogInteractionListener = dialogInteractionListener
    }

    // endregion
    // region Utility Methods
    private fun search(searchQuery: String) {
        searchResults!!.clear()
        for (state: State in dialogInteractionListener!!.allStates!!) {
            if (state.stateName!!.toLowerCase(Locale.ENGLISH).contains(searchQuery.toLowerCase())) {
                searchResults!!.add(state)
            }
        }
        dialogInteractionListener!!.sortStates((searchResults)!!)
        adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        searchResults = ArrayList()
        searchResults!!.addAll((dialogInteractionListener!!.allStates)!!)
        adapter = StateAdapter(getActivity()!!, searchResults!!, this)
        statesRecyclerView!!.setHasFixedSize(true)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(getContext())
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        statesRecyclerView!!.setLayoutManager(layoutManager)
        statesRecyclerView!!.setAdapter(adapter)
    }

    // endregion
    //region Interface
    interface StatePickerDialogInteractionListener {
        val allStates: List<State>?
        fun sortStates(searchResults: MutableList<State>)
        fun canSearch(): Boolean
    }

    companion object {
        // endregion
        // region Constructors
        fun newInstance(): StatePickerDialog {
            return StatePickerDialog()
        }
    }
}