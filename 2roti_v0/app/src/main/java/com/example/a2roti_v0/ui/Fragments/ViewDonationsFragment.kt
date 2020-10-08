package com.example.a2roti_v0.ui.Fragments


import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2roti_v0.Adapter.DonationsAdapter
import com.example.a2roti_v0.Model.Donation

import com.example.a2roti_v0.R
import com.example.a2roti_v0.ui.viewDetailsActivity
import com.example.a2roti_v0.utils.toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_view_donations.*

/**
 * A simple [Fragment] subclass.
 */
class ViewDonationsFragment : Fragment(),DonationsAdapter.OnDonationclickListener {
    val db=Firebase.firestore
    var adapterr:DonationsAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_view_donations, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(0)
    }


    private fun setUpRecyclerView(flag:Int) {

        db.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener {
                val city_id=it.getLong("city_id")!!
                Log.d("Donation Recycler View","City ID found from it is:   "+ city_id.toString())
                var query:Query = db.collection("Donators").whereEqualTo("city_id", city_id)
                if(flag==3) {
                    query = db.collection("Donators").whereEqualTo("city_id", city_id).whereEqualTo("category","Books")
                }
                else if(flag==1)
                    query=db.collection("Donators").whereEqualTo("city_id", city_id).whereEqualTo("category","Food")
                else if(flag==2)
                {
                    query=db.collection("Donators").whereEqualTo("city_id", city_id).whereEqualTo("category","Clothes")
                }
                val options= FirestoreRecyclerOptions.Builder<Donation>()
                    .setQuery(query,Donation::class.java)
                    .setLifecycleOwner(this)
                    .build()
                adapterr= DonationsAdapter(options,this)
                donations_recyclerview.layoutManager = LinearLayoutManager(activity)
                donations_recyclerview.adapter=adapterr
            }
            .addOnFailureListener{
                activity!!.toast(it.message.toString())
            }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filter_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_food -> {
                setUpRecyclerView(1)
                true
            }
            R.id.filter_cloth -> {
                setUpRecyclerView(2)
                true
            }
            R.id.filter_books->{
               setUpRecyclerView(3)
                true
            }
            R.id.filter_clear->{
                setUpRecyclerView(0)
                true
            }
            else -> false
        }


    }
    override fun OnDonationClick(documentSnapshot: DocumentSnapshot?, position: Int) {
        activity!!.toast(documentSnapshot!!.reference.path)

        startActivity(Intent(activity, viewDetailsActivity::class.java).putExtra("id",documentSnapshot.id).putExtra("flag",true))
    }


}
