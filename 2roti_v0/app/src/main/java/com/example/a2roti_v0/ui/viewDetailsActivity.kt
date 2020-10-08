package com.example.a2roti_v0.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.a2roti_v0.R
import com.example.a2roti_v0.utils.toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_view_details.*

class viewDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)
        val id=intent.getStringExtra("id")
        val flag=intent.getBooleanExtra("flag",true)
        val db=Firebase.firestore
        var collection:CollectionReference
        if(flag)
        {
            collection=db.collection("Donators")
        }
        else
        {
            collection=db.collection("Requestors")
        }
        collection.document(id).get()
            .addOnSuccessListener {
                documentSnapshot ->
                details_text1_name.text=documentSnapshot.getString("user_name")
                details_text1_organization_name.text=documentSnapshot.getString("organization_name")
                details_text1_phone.text=documentSnapshot.getString("phone_no")
                details_text1_category.text=documentSnapshot.getString("category")
                details_text1_detail.text=documentSnapshot.getString("details")
                details_text1_address.text=documentSnapshot.getString("address")
                details_text1_landmark.text=documentSnapshot.getString("landmark")
                details_text_comment.text=documentSnapshot.getString("comments")
            }
            .addOnFailureListener {
                toast(it.message.toString())
                Log.d("View Details Activity", it.message)
            }

    }
}
