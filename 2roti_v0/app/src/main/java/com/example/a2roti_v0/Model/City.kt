package com.example.a2roti_v0.Model

class City {
    var cityId:Long = 0
    var cityName: String? = null
    var stateId = 0

    constructor() {}
    constructor(cityId: Long, cityName: String?, stateId: Int) {
        this.cityId = cityId
        this.cityName = cityName
        this.stateId = stateId
    }

}