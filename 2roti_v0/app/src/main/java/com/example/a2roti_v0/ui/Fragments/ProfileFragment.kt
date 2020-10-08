package com.example.a2roti_v0.ui.Fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.channels.AsynchronousFileChannel.open
import java.util.ArrayList
import com.example.a2roti_v0.utils.toast
import com.google.firebase.auth.UserProfileChangeRequest

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment(),OnStatePickerListener,OnCityPickerListener,OnCountryPickerListener {
    private var pickStateButton: Button? = null
    private var pickCountry: Button? = null
    private var pickCity: Button? = null
    private var stateNameTextView: TextView? = null
    private var countryName: TextView? = null
    private var cityName: TextView? = null

    // Pickers
    private var countryPicker: CountryPicker? = null
    private var statePicker: StatePicker? = null
    private var cityPicker: CityPicker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser
        text_phone.setOnClickListener {
            val action = ProfileFragmentDirections.actionVerifyPhone()
            Navigation.findNavController(it).navigate(action)
        }
        val db=Firebase.firestore
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
        // initialize listeners
        setListener()
        setCountryListener()
        setCityListener()
        button_save.setOnClickListener{
            val name = edit_text_name.text.toString().trim()
            context?.toast(name)
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
            if(statename.isEmpty() || statename=="Region")
            {
                stateNameTextView!!.error = "State Required"
                stateNameTextView!!.requestFocus()
                return@setOnClickListener
            }
            val cityname = cityName?.text.toString().trim()
            if(cityname.isEmpty() || cityname =="City")
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
                    context?.toast("document added successfully")
//                    progressbar.visibility=View.GONE
                }
                .addOnFailureListener{
                        e->
                    Log.d("Registeration Activity","Error Adding Document"+e)
                    context?.toast("Document not added Sucessfully!!")
//                    progressbar.visibility=View.GONE
                }
            val update = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            currentUser.updateProfile(update)
                .addOnCompleteListener{ task->
                    progressbar.visibility= View.GONE
                    if(task.isSuccessful)
                    {
                        context?.toast("Profile Updated")

                    }
                    else
                    {
                        context?.toast(task.exception?.message!!)
                    }
                }
            initView()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
    }


    fun initView() {
        //Buttons
        pickStateButton = view?.findViewById<View>(R.id.pickState) as Button
        //set state picker invisible
        pickStateButton!!.visibility = View.INVISIBLE
        pickCountry = view?.findViewById<View>(R.id.pickCountry) as Button
        pickCity = view?.findViewById<View>(R.id.pick_city) as Button
        // set city picker invisible
        pickCity!!.visibility = View.INVISIBLE
        // Text Views
        countryName = view?.findViewById<View>(R.id.countryNameTextView) as TextView
        stateNameTextView = view?.findViewById<View>(R.id.state_name) as TextView
        //set state name text view invisible
        stateNameTextView!!.visibility = View.VISIBLE
        cityName = view?.findViewById<View>(R.id.city_name) as TextView
        //set state name text view invisible
        cityName!!.visibility = View.VISIBLE
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db=Firebase.firestore
        currentUser?.let {user->
            db.collection("Users")
                .whereEqualTo("user_id",user.uid)
                .get()
                .addOnSuccessListener { users->
                    for(use in users)
                    {
                        edit_text_name.setText(use.get("user_name").toString())
                        text_phone.text = use.get("phone_no").toString()
                        countryNameTextView.text = use.get("country_name").toString()
                        state_name.text = use.get("state_name").toString()
                        city_name.text = use.get("city_name").toString()
                        cityID= use.getLong("city_id")!!
                    }
                }
                .addOnFailureListener{
                    Log.d("Profile Fragment","Details of Users  not retrived due to folloeing error " + it.toString())
                }
        }

        // initiate state object, parser, and arrays
        stateObject = ArrayList()
        cityObject = ArrayList()

        // initialize country picker
        countryPicker = CountryPicker.Builder().with(activity!!.applicationContext).listener(this).build()

    }

    // SET STATE LISTENER
    private fun setListener() {
        pickStateButton!!.setOnClickListener { statePicker!!.showDialog(activity!!.supportFragmentManager) }
    }

    //SET COUNTRY LISTENER
    private fun setCountryListener() {
        pickCountry!!.setOnClickListener { countryPicker!!.showDialog(activity!!.supportFragmentManager) }
    }

    //SET CITY LISTENER
    private fun setCityListener() {
        pickCity!!.setOnClickListener { cityPicker!!.showDialog(activity!!.supportFragmentManager) }
    }

    @get:Throws(JSONException::class)
    val stateJson: Unit
        get() {
            var json: String? = null
            try {
                val inputStream = activity!!.assets.open("states.json")
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
                val inputStream = activity!!.assets.open("Indian_cities.json")
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
            cityPicker = CityPicker.Builder().with(context!!).listener(this).build()
            val cityData = City()
            if (cityObject!![i].stateId == stateID) {
                cityData.cityId = cityObject!![i].cityId
                cityData.cityName = cityObject!![i].cityName
                cityData.stateId = cityObject!![i].stateId
                CityPicker.equalCityObject.add(cityData)
            }
        }
    }

    override fun onSelectCity(city: City?) {
        cityName!!.text = city!!.cityName
        cityID= city.cityId
    }

    override fun onSelectCountry(country: Country?) {
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

        // GET STATES OF SELECTED COUNTRY
        for (i in stateObject!!.indices) {
            // init state picker
            statePicker = StatePicker.Builder().with(context!!).listener(this).build()
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

    companion object {
        var countryID = 0
        var stateID = 0
        var cityID:Long =0
        // arrays of state object
        var stateObject: MutableList<State>? = null

        // arrays of city object
        var cityObject: MutableList<City>? = null
    }
}
