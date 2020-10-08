package com.example.a2roti_v0.ui.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.a2roti_v0.R
import com.example.a2roti_v0.utils.toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_verify_phone.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class VerifyPhoneFragment : Fragment() {
    private var verifyId:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutPhone.visibility = View.VISIBLE
        layoutVerification.visibility=View.GONE

        button_send_verification.setOnClickListener{

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
                    requireActivity(),
                    phoneAuthCallback

                )

            layoutPhone.visibility=View.GONE
            layoutVerification.visibility=View.VISIBLE
        }

        button_verify.setOnClickListener{
            val code= edit_text_code.text.toString().trim()
            if(code.isEmpty())
            {
                edit_text_code.error="Code Required!!"
                edit_text_code.requestFocus()
                return@setOnClickListener
            }
            verifyId?.let {
                val credential=PhoneAuthProvider.getCredential(it,code)
                addPhoneNo(credential)
            }

        }
    }

    private val phoneAuthCallback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            phoneAuthCredential?.let {
                addPhoneNo(it)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {

            context?.toast(exception.message!!)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            verifyId=verificationId


        }

    }

    private fun addPhoneNo(it: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(it)
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful)
                {
                    context?.toast("Phone Updated")
                    val action = VerifyPhoneFragmentDirections.actionVerifiedPhone()
                    Navigation.findNavController(button_verify).navigate(action)
                }
                else
                {
                    context?.toast(task.exception?.message!!)
                }
            }
    }

}
