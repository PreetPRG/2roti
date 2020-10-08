package com.example.a2roti_v0.ui.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.a2roti_v0.R
import com.example.a2roti_v0.ui.FormActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_donate.setOnClickListener {
            startActivity(Intent(activity, FormActivity::class.java).putExtra("flag",true))
        }
        button_request.setOnClickListener {
            startActivity(Intent(activity, FormActivity::class.java).putExtra("flag",false))
        }
    }
}
