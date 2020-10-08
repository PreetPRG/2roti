package com.example.a2roti_v0.Model

import android.content.Context
import java.util.*

class State {
    // endregion
    // region Getter/Setter
    // region Variables
    var stateId = 0
    var stateName: String? = null
    var countryId = 0
    var flag = 0

    // endregion
    // region Constructors
    constructor() {}
    internal constructor(stateId: Int, stateName: String?, countryId: Int, flag: Int) {
        this.stateId = stateId
        this.stateName = stateName
        this.countryId = countryId
        this.flag = flag
        //  this.flag = flag;
    }

    fun loadFlagByCode(context: Context) {
        if (flag != -1) {
            return
        }
        try {
            flag = context.resources
                .getIdentifier(
                    "flag_" + stateName!!.toLowerCase(Locale.ENGLISH), "drawable",
                    context.packageName
                )
        } catch (e: Exception) {
            e.printStackTrace()
            flag = -1
        }
    }
}