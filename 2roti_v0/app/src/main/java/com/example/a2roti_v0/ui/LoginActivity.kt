package com.example.a2roti_v0.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a2roti_v0.R
import com.example.a2roti_v0.utils.login
import com.example.a2roti_v0.utils.toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit



class LoginActivity : AppCompatActivity(){

    private lateinit var mAuth: FirebaseAuth
    private var verifyId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        layoutVerification.visibility= View.GONE
        layoutPhone.visibility = View.VISIBLE


        button_sign_in.setOnClickListener{
            val phone=edit_text_phone.text.toString().trim()

            if(phone.isEmpty() || phone.length != 10)
            {
                edit_text_phone.error = "Enter a Valid Phone no"
                edit_text_phone.requestFocus()
                return@setOnClickListener
            }

            val phoneNumber = "+91" + phone

            PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                    phoneNumber,
                    2,
                    TimeUnit.MINUTES,
                    this,
                    phoneAuthCallback

                )

            layoutPhone.visibility=View.GONE
            layoutVerification.visibility=View.VISIBLE
            //check for phone auth
        }
        //text_view_register.setOnClickListener { startActivity(Intent(this@LoginActivity,RegisterationActivity::class.java)); }
    }

    private val phoneAuthCallback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            phoneAuthCredential.let {
                addPhoneNo(it)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {

            toast(exception.message!!)

        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            verifyId=verificationId


        }

    }

    private fun addPhoneNo(it: PhoneAuthCredential) {
        mAuth.signInWithCredential(it)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    if(task.getResult()?.additionalUserInfo?.isNewUser!!)
                    {

                        Log.d("Registeration Activity","New User Signed In..")
                        startActivity(Intent(this@LoginActivity,RegisterationActivity::class.java))

                    }
                    else{
                        login()
                    }
                }
                else
                {
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
                mAuth.currentUser?.let {
            login()
            toast("User already present")
        }

    }
}
