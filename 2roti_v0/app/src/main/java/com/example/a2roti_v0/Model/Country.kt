package com.example.a2roti_v0.Model

import android.content.Context
import android.text.TextUtils
import java.util.*

class Country {
    // endregion
    // region Getter/Setter
    // region Variables
    var countryId = 0
    private var code: String? = null
    var name: String? = null
    var dialCode: String? = null
    var flag = 0
    var currency: String? = null

    // endregion
    // region Constructors
    constructor() {}
    constructor(
        countryId: Int,
        code: String?,
        name: String?,
        dialCode: String?,
        flag: Int,
        currency: String?
    ) {
        this.countryId = countryId
        this.code = code
        this.name = name
        this.dialCode = dialCode
        this.flag = flag
        this.currency = currency
    }

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String) {
        this.code = code
        if (TextUtils.isEmpty(name)) {
            name = Locale("", code).displayName
        }
    }

    fun loadFlagByCode(context: Context) {
        if (flag != -1) {
            return
        }
        try {
            flag = context.resources
                .getIdentifier(
                    "flag_" + code!!.toLowerCase(Locale.ENGLISH), "drawable",
                    context.packageName
                )
        } catch (e: Exception) {
            e.printStackTrace()
            flag = -1
        }
    } // endregion
}