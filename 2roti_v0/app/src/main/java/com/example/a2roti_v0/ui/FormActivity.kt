package com.example.a2roti_v0.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import com.example.a2roti_v0.R
import com.example.a2roti_v0.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.view.*

class FormActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener{
    var category:String? = null
    var details:String?= null
    var address:String? = null
    var comments:String? = null
    var landmark:String? = null
    var organization_name:String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val flag = intent.getBooleanExtra("flag",true)
        if(flag==false)
        {
            text_head.setText("Request Form")
        }


        val spinner: Spinner =findViewById(R.id.planets_spinner)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        val db=Firebase.firestore
        val currentuser=FirebaseAuth.getInstance().currentUser
        var cityID:Long? = 0
        var phoneNo:String?= ""
        db.collection("Users").document(currentuser!!.uid)
            .get()
            .addOnSuccessListener { user->
             cityID=   user.getLong("city_id")
             phoneNo=  user.getString("phone_no")
            }
            .addOnFailureListener{
                exception ->
                toast(exception.message.toString())
            }

        button_submit_donation_form.setOnClickListener {
            details=edit_text_content.text.toString()
            address=edit_text_address.text.toString()
            comments=edit_text_comment.text.toString()
            landmark=edit_text_landmark.text.toString()
            val organization=checkbox_organization.isChecked
            if(organization)
            {
                organization_name=edit_text_organization_name.text.toString()
                if(organization_name.isNullOrEmpty()) {
                    edit_text_organization_name.error = "Please enter organization Name"
                    edit_text_organization_name.requestFocus()
                    return@setOnClickListener
                }
            }
            else
            {
                organization_name="Not Organization"
            }
            if(details!!.isEmpty())
            {
                edit_text_content.error="Please fill the details"
                edit_text_content.requestFocus()
                return@setOnClickListener
            }
            if(address!!.isEmpty())
            {
                edit_text_address.error="Please fill the address"
                edit_text_address.requestFocus()
                return@setOnClickListener
            }
            if(comments!!.isEmpty())
            {
                edit_text_comment.error="Please fill any commnent"
                edit_text_comment.requestFocus()
                return@setOnClickListener
            }
            if(landmark!!.isEmpty())
            {
                edit_text_landmark.error="Please add landmark"
                edit_text_landmark.requestFocus()
                return@setOnClickListener
            }


            val donate = hashMapOf(
                "user_id" to currentuser.uid,
                "user_name" to currentuser.displayName,
                "phone_no" to phoneNo,
                "city_id" to cityID,
                "organization" to organization,
                "organization_name" to organization_name,
                "details" to details,
                "address" to address,
                "landmark" to landmark,
                "comments" to comments?.let{comments},
                "category" to category
            )
            //Adding document to data base and checking validity clauses is remaining
            if(flag) {
                db.collection("Donators").add(donate)
                    .addOnSuccessListener {
                        Log.d(
                            "Forms Activity",
                            "Donate document added and the document reference is" + it
                        )
                        toast("Added successfully!")
                    }
                    .addOnFailureListener { exception ->
                        toast(exception.message.toString())
                    }

            }
            else
            {
                db.collection("Requestors").add(donate)
                    .addOnSuccessListener {
                        Log.d(
                            "Forms Activity",
                            "Donate document added and the document reference is" + it
                        )
                        toast("Added successfully!")
                    }
                    .addOnFailureListener { exception ->
                        toast(exception.message.toString())
                    }
            }
            startActivity(Intent(this@FormActivity, HomeActivity::class.java))
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            if(checked)
            {
                edit_text_organization_name.visibility=View.VISIBLE
            }
            else
            {
                edit_text_organization_name.visibility=View.GONE
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        category = parent?.getItemAtPosition(position).toString()
        if(category=="Food")
        {
            edit_text_content.setHint("Food Can Serve: #, Cooked food or Raw Food,\ne.g. Grain: #kg or Roti: #")
        }
        else if(category=="Clothes")
        {
            edit_text_content.setHint("No of Clothes: #, Detail of Cloth: ")
        }
        else if(category=="books")
        {
            edit_text_content.setHint("Please add details about the books")
        }
        else
        {
            edit_text_content.setHint("Please add complete details")
        }
    }
}
