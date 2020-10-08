package com.example.a2roti_v0.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.a2roti_v0.ui.HomeActivity
import com.example.a2roti_v0.ui.LoginActivity

fun Context.toast(message: String)=
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
fun Context.login(){
    val intent = Intent(this, HomeActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    }
    startActivity(intent)
}

fun Context.logout(){
    val intent = Intent(this, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    }
    startActivity(intent)
}
