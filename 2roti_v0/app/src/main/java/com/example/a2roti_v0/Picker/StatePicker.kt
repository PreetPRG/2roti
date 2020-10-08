package com.example.a2roti_v0.Picker

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.a2roti_v0.Interfaces.OnStatePickerListener
import com.example.a2roti_v0.Model.State
import com.example.a2roti_v0.R
import java.util.*

class StatePicker : StatePickerDialog.StatePickerDialogInteractionListener {
    val STATES: Array<State> = equalStateObject.toTypedArray()
    private var context: Context? = null
    private var sortBy: Int = SORT_BY_NONE
    private var onStatePickerListener: OnStatePickerListener? = null
    private var canSearch: Boolean = true
    private var states: MutableList<State>? = null

    // endregion
    // region Constructors
    private constructor() {}
    internal constructor(builder: Builder) {
        sortBy = builder.sortBy
        if (builder.onStatePickerListener != null) {
            onStatePickerListener = builder.onStatePickerListener
        }
        context = builder.context
        canSearch = builder.canSearch
        states = ArrayList(Arrays.asList(*STATES))
        sortStates(states!!)
    }

    // endregion
    // region Listeners
    public override fun sortStates(states: MutableList<State>) {
        when (sortBy) {
            SORT_BY_NAME -> Collections.sort(states, object : Comparator<State> {
                public override fun compare(state1: State, state2: State): Int {
                    return state1.stateName!!.trim({ it <= ' ' }).compareTo(state2.stateName!!.trim({ it <= ' ' }), ignoreCase = true)
                }
            })
        }
    }

    override val allStates: List<State>?
        get() {
            return states
        }

    public override fun canSearch(): Boolean {
        return canSearch
    }

    // endregion
    // region Utility Methods
    // region Utility Methods
    fun showDialog(supportFragmentManager: FragmentManager) {
        if (states == null || states!!.isEmpty()) {
            throw IllegalArgumentException(context!!.getString(R.string.error_no_states_found))
        } else {
            val statePickerDialog: StatePickerDialog = StatePickerDialog.Companion.newInstance()
            if (onStatePickerListener != null) {
                statePickerDialog.setStatePickerListener(onStatePickerListener)
                //.setStatePickerListener(onStatePickerListener);
            }
            statePickerDialog.setDialogInteractionListener(this)
            statePickerDialog.show(supportFragmentManager, STATE_TAG)
        }
    }

    fun setStates(states: List<State>) {
        this.states!!.clear()
        this.states!!.addAll(states)
        sortStates((this.states)!!)
    }

    fun getStateByName(stateNamee: String): State? {
        var stateName: String = stateNamee
        stateName = stateName.toUpperCase()
        val state: State = State()
        state.stateName=stateName
        val i: Int = Arrays.binarySearch(STATES, state, NameComparator())
        if (i < 0) {
            return null
        } else {
            return STATES.get(i)
        }
    }

    // endregion
    // region Builder
    class Builder constructor() {
        var context: Context? = null
        var sortBy: Int = SORT_BY_NONE
        var canSearch: Boolean = true
        var onStatePickerListener: OnStatePickerListener? = null
        fun with(context: Context): Builder {
            this.context = context
            return this
        }

        fun sortBy(sortBy: Int): Builder {
            this.sortBy = sortBy
            return this
        }

        fun listener(onStatePickerListener: OnStatePickerListener): Builder {
            this.onStatePickerListener = onStatePickerListener
            return this
        }

        fun canSearch(canSearch: Boolean): Builder {
            this.canSearch = canSearch
            return this
        }

        fun build(): StatePicker {
            return StatePicker(this)
        }
    }

    // endregion
    class NameComparator constructor() : Comparator<State> {
        public override fun compare(state: State, nextState: State): Int {
            return state.stateName!!.compareTo(nextState.stateName!!)
        }
    }

    companion object {
        // region states
        var equalStateObject: MutableList<State> = ArrayList()

        // region Variables
        val SORT_BY_NONE: Int = 0
        val SORT_BY_NAME: Int = 1
        val SORT_BY_COUNTRY: Int = 2
        private val STATE_TAG: String = "STATE_PICKER"
    }
}