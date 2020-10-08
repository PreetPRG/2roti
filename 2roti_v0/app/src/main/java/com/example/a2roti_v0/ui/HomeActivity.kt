package com.example.a2roti_v0.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.a2roti_v0.R
import com.example.a2roti_v0.utils.logout
import com.example.a2roti_v0.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navController = Navigation.findNavController(this,R.id.fragment)

        NavigationUI.setupWithNavController(nav_view,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this,R.id.fragment),
            drawer_layout
        )
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//        menuInflater.inflate(R.menu.option_menu,menu)
//        return true
//    }
//override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    if(item.itemId == R.id.action_logout)
//    {
//        AlertDialog.Builder(this).apply {
//            setTitle("Are you sure to logout??")
//            setPositiveButton("Yes"){_,_ ->
//                FirebaseAuth.getInstance().signOut()
//                logout()
//            }
//            setNegativeButton("Cancel"){_,_ ->
//
//            }
//        }.create().show()
//    }
//    else if(item.itemId == R.id.action_delete)
//    {
//
//        val db=Firebase.firestore
//
//
//        FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener{
//                task ->
//            if(task.isSuccessful)
//            {
//                db.collection("Users").whereEqualTo("user_id",FirebaseAuth.getInstance().currentUser?.uid)
//                    .get()
//                    .addOnSuccessListener { documents->
//                        for (document in documents)
//                        {
//                            db.collection("Users").document(document.id).delete()
//                            //Log.d("Home Activityyy",document.getDocumentReference().toString())
//                        }
//                    }
//                    .addOnFailureListener{
//                        toast(it.message!!)
//                    }
//                toast("User Deleted Successfully!!")
//                startActivity(Intent(this@HomeActivity,LoginActivity::class.java))
//            }
//            else
//            {
//                toast(task.exception?.message!!)
//            }
//        }
//
//    }
//    return super.onOptionsItemSelected(item)
//}

}
