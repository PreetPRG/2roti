package com.example.a2roti_v0.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.a2roti_v0.Adapter.CityPicker
import com.example.a2roti_v0.Interfaces.OnCityPickerListener
import com.example.a2roti_v0.Interfaces.OnCountryPickerListener
import com.example.a2roti_v0.Interfaces.OnStatePickerListener
import com.example.a2roti_v0.Model.City
import com.example.a2roti_v0.Model.Country
import com.example.a2roti_v0.Model.State
import com.example.a2roti_v0.Picker.CountryPicker
import com.example.a2roti_v0.Picker.StatePicker
import com.example.a2roti_v0.R
import com.example.a2roti_v0.utils.login
import com.example.a2roti_v0.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_registeration.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class RegisterationActivity : AppCompatActivity(),OnStatePickerListener,OnCountryPickerListener,OnCityPickerListener {

    private var pickStateButton: Button? = null
    private var pickCountry: Button? = null
    private var pickCity: Button? = null
    private var stateNameTextView: TextView? = null
    private var countryName: TextView? = null
    private var countryCode: TextView? = null
    private var countryPhoneCode: TextView? = null
    private var countryCurrency: TextView? = null
    private var cityName: TextView? = null
    private var flagImage: ImageView? = null

    // Pickers
    private var countryPicker: CountryPicker? = null
    private var statePicker: StatePicker? = null
    private var cityPicker: CityPicker? = null
    //val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        initView()
        try {
            stateJson
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        // get City from assets JSON
        try {
            cityJson
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        // initialize country picker
        countryPicker = CountryPicker.Builder().with(this).listener(this).build()


        // initialize listeners
        setListener()
        setCountryListener()
        setCityListener()

        button_save.setOnClickListener{
            val name = text_email.text.toString().trim()
            toast(name)
            if(name.isEmpty())
            {
                edit_text_name.error = "Name Required"
                edit_text_name.requestFocus()
                return@setOnClickListener
            }
            val countryname = countryName?.text.toString().trim()
            if(countryname.isEmpty())
            {
                countryName!!.error = "Country Required"
                countryName!!.requestFocus()
                return@setOnClickListener
            }
            val statename = stateNameTextView?.text.toString().trim()
            if(statename.isEmpty())
            {
                stateNameTextView!!.error = "State Required"
                stateNameTextView!!.requestFocus()
                return@setOnClickListener
            }
            val cityname = cityName?.text.toString().trim()
            if(cityname.isEmpty())
            {
                cityName!!.error = "City Required"
                cityName!!.requestFocus()
                return@setOnClickListener
            }

            //countryPicker.getCountryByISO(countryPhoneCode.toString())


            progressbar.visibility = View.VISIBLE

            val user = hashMapOf(
                "user_id" to currentUser?.uid,
                "user_name" to name,
                "phone_no" to currentUser?.phoneNumber,
                "country_name" to countryname,
                "state_name" to statename,
                "city_name" to cityname,
                "city_id" to cityID
            )

            db.collection("Users").document(currentUser!!.uid)
                .set(user)
                .addOnSuccessListener {documentReference->
                    Log.d("Registerateion Activity", "Document ID : ${documentReference}")
                    toast("document added successfully")
                }
                .addOnFailureListener{
                    e->
                    Log.d("Registeration Activity","Error Adding Document"+e)
                    toast("Document not added Sucessfully!!")
                }

            val update = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            currentUser.updateProfile(update)
                .addOnCompleteListener{ task->
                    progressbar.visibility= View.GONE
                    if(task.isSuccessful)
                    {
                        login()
                        toast("Profile Updated")

                    }
                    else
                    {
                        toast(task.exception?.message!!)
                    }
                }

        }
    }

    fun initView() {
        //Buttons
        pickStateButton = findViewById<View>(R.id.pickState) as Button
        //set state picker invisible
        pickStateButton!!.visibility = View.INVISIBLE
        pickCountry = findViewById<View>(R.id.pickCountry) as Button
        pickCity = findViewById<View>(R.id.pick_city) as Button
        // set city picker invisible
        pickCity!!.visibility = View.INVISIBLE
        // Text Views
        countryName = findViewById<View>(R.id.countryNameTextView) as TextView
        countryCode = findViewById<View>(R.id.countryCodeTextView) as TextView
        countryPhoneCode = findViewById<View>(R.id.countryDialCodeTextView) as TextView
        countryCurrency = findViewById<View>(R.id.countryCurrencyTextView) as TextView
        stateNameTextView = findViewById<View>(R.id.state_name) as TextView
        //set state name text view invisible
        stateNameTextView!!.visibility = View.INVISIBLE
        cityName = findViewById<View>(R.id.city_name) as TextView
        //set state name text view invisible
        cityName!!.visibility = View.INVISIBLE

        // ImageView
        flagImage = findViewById<View>(R.id.flag_image) as ImageView

        // initiate state object, parser, and arrays
        stateObject = ArrayList()
        cityObject = ArrayList()
    }

    // SET STATE LISTENER
    private fun setListener() {
        pickStateButton!!.setOnClickListener { statePicker!!.showDialog(supportFragmentManager) }
    }

    //SET COUNTRY LISTENER
    private fun setCountryListener() {
        pickCountry!!.setOnClickListener { countryPicker!!.showDialog(supportFragmentManager) }
    }

    //SET CITY LISTENER
    private fun setCityListener() {
        pickCity!!.setOnClickListener { cityPicker!!.showDialog(supportFragmentManager) }
    }//JSONArray events = new JSONArray(json);

    @get:Throws(JSONException::class)
    val stateJson: Unit
        get() {
            var json: String? = null
            try {
                val inputStream = assets.open("states.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //Log.d("json object result", json.toString())
            //JSONArray events = new JSONArray(json);
            val jsonObject = JSONObject(json!!)
            val events = jsonObject.getJSONArray("states")
            for (j in 0 until events.length()) {
                val cit = events.getJSONObject(j)
                val stateData = State()
                stateData.stateId = cit.getInt("id")
                stateData.stateName = cit.getString("name")
                stateData.countryId = cit.getInt("country_id")
                stateObject!!.add(stateData)
            }
        }//JSONArray events = new JSONArray(json);

    // GET CITY FROM ASSETS JSON
    @get:Throws(JSONException::class)
    val cityJson: Unit
        get() {
            var json: String? = null
            try {
                val inputStream = assets.open("Indian_cities.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer,Charsets.UTF_8)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //Log.d("json object result", json.toString())
            //JSONArray events = new JSONArray(json);
            val jsonObject = JSONObject(json!!)
            val events = jsonObject.getJSONArray("cities")
            for (j in 0 until events.length()) {
                val cit = events.getJSONObject(j)
                val cityData = City()
                cityData.cityId = cit.getLong("id")
                cityData.cityName = cit.getString("name")
                cityData.stateId = cit.getInt("state_id")
                cityObject!!.add(cityData)
            }
        }



    override fun onSelectState(state: State?) {
        pickCity!!.visibility = View.VISIBLE
        cityName!!.visibility = View.VISIBLE
        cityName!!.text = "City"
        CityPicker.equalCityObject.clear()
        stateNameTextView!!.text = state!!.stateName
        stateID = state.stateId
        for (i in cityObject!!.indices) {
            cityPicker = CityPicker.Builder().with(this).listener(this).build()
            val cityData = City()
            if (cityObject!![i].stateId == stateID) {
                cityData.cityId = cityObject!![i].cityId
                cityData.cityName = cityObject!![i].cityName
                cityData.stateId = cityObject!![i].stateId
                CityPicker.equalCityObject.add(cityData)
            }
        }
    }

    override fun onBackPressed() {

    }
    override fun onSelectCountry(country: Country?) {
        // get country name and country ID
        countryName!!.text = country!!.name
        countryID = 101
        Log.d("Country Id", countryID.toString())
        StatePicker.equalStateObject.clear()
        CityPicker.equalCityObject.clear()

        //set state name text view and state pick button invisible
        pickStateButton!!.visibility = View.VISIBLE
        stateNameTextView!!.visibility = View.VISIBLE
        stateNameTextView!!.text = "Region"
        cityName!!.text = "City"
        // set text on main view
        countryCode!!.text = "Country code: " + country.getCode()
        countryPhoneCode!!.text = "Country dial code: " + country.dialCode
        countryCurrency!!.text = "Country currency: " + country.currency
        flagImage!!.setBackgroundResource(country.flag)


        // GET STATES OF SELECTED COUNTRY
        for (i in stateObject!!.indices) {
            // init state picker
            statePicker = StatePicker.Builder().with(this).listener(this).build()
            val stateData = State()
            if (stateObject!![i].countryId == countryID) {
                stateData.stateId = stateObject!![i].stateId
                stateData.stateName = stateObject!![i].stateName
                stateData.countryId = stateObject!![i].countryId
                stateData.flag = country.flag
                StatePicker.equalStateObject.add(stateData)
            }
        }
    }

    override fun onSelectCity(city: City?) {
        cityName!!.text = city!!.cityName
        cityID=city.cityId
    }

    companion object {
        var countryID = 0
        var stateID = 0
        var cityID:Long = 0
        // arrays of state object
        var stateObject: MutableList<State>? = null

        // arrays of city object
        var cityObject: MutableList<City>? = null
    }

}
